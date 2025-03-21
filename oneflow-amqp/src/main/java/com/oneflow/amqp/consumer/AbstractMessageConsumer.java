package com.oneflow.amqp.consumer;

import com.oneflow.amqp.constant.MQConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractMessageConsumer<T> {

    @Resource
    private RedisUtil redisUtil;


    public void handleMessage(T messageObject, Message message, Channel channel)  {
        String messageId = message.getMessageProperties().getMessageId();
        log.info("AbstractMessageConsumer handleMessage messageId:{}", messageId);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String consumeKey = MQConstants.BASE_KEY + messageId;

        // 1. 处理重复消费的消息
        if (Boolean.TRUE.equals(redisUtil.hasKey(consumeKey))) {
            log.info("AbstractMessageConsumer handleMessage message is processing:{}", messageId);
            return;
        }

        try {
            // 2. 将消息存入redis中，避免重复消费
            redisUtil.set(consumeKey, messageId, 60, TimeUnit.SECONDS);

            // 3. 处理消息
            processMessage(messageObject);

            // 4. 消息处理成功后，删除redis中的key
            redisUtil.delete(consumeKey);

            // 5. 手工确认消息已经处理成功
            channel.basicAck(deliveryTag, true);

        } catch (Exception e) {
            log.error("AbstractMessageConsumer handleMessage messageId:{} error:", messageId, e);

            // 6. 消息处理失败，删除redis中的key
            redisUtil.delete(consumeKey);

            // 7. 消息处理失败，重新放回队列，进行重试，超过重试次数后，放入死信队列
            retryAck(messageId, channel, deliveryTag, e.getMessage());
        }
    }

    /**
     * 处理死信消息
     * @param messageObject
     * @param message
     * @param channel
     */
    public void handleDeadLetterMessage(T messageObject, Message message, Channel channel) {
        String messageId = message.getMessageProperties().getMessageId();
        log.info("AbstractMessageConsumer handleDeadLetterMessage messageId:{}", messageId);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String consumeKey = MQConstants.BASE_KEY + messageId;

        // 1. 处理重复消费的消息
        if (Boolean.TRUE.equals(redisUtil.hasKey(consumeKey))) {
            log.info("AbstractMessageConsumer handleDeadLetterMessage message is processing:{}", messageId);
            return;
        }

        try {
            // 2. 将消息存入redis中，避免重复消费
            redisUtil.set(consumeKey, messageId, 60, TimeUnit.SECONDS);

            // 3. 处理死信消息
            processDeadLetterMessage(messageObject);

            // 4. 消息处理成功后，删除redis中的key
            redisUtil.delete(consumeKey);

            // 5. 手工确认消息已经处理成功
            channel.basicAck(deliveryTag, true);

        } catch (Exception e) {
            log.error("AbstractMessageConsumer handleDeadLetterMessage messageId:{} error:", messageId, e);

            // 6. 消息处理失败，删除redis中的key
            redisUtil.delete(consumeKey);

            // 7. 死信消息处理失败，直接丢弃
            try {
                channel.basicReject(deliveryTag, false);
            } catch (IOException ex) {
                log.error("Failed to reject message with deliveryTag: {}", deliveryTag, ex);
            }
        }
    }



    /**
     * 处理消息，具体处理由子类实现，这里消息处理成功后添加处理成功标记
     *
     * @param messageObject
     */
    public void processMessage(T messageObject) {

        /**
         * 如果子类重写了这个方法，那么就只会执行子类中这个方法中的代码，如果想执行这段代码，可以使用super.processMessage(messageObject);来调用这段代码
         *
         * 子类可以重写父类中非 final 和非 private 的方法。
         */

        if (messageObject == null) {
            return;
        }

        if (messageObject instanceof OrderDO) {
            OrderDO orderDO = (OrderDO) messageObject;
            log.info("AbstractMessageConsumer handleMessage orderDO:{}", orderDO.getOrderNo());

            // 对处理完的消息标记已处理

        }
    }

    /**
     * 处理死信消息，由子类实现
     *
     * @param messageObject
     */
    protected abstract void processDeadLetterMessage(T messageObject);

    /**
     * 处理异常消息，由子类实现
     *
     * @param messageObject
     * @param e
     */
    protected abstract void processException(T messageObject, Exception e);


    /**
     * 重试
     *
     * @param messageId
     * @param channel
     * @param deliverTag
     */
    private void retryAck(String messageId, Channel channel, long deliverTag, String errMsg) {
        try {
            String key = MQConstants.RETRY + messageId;

            int retryCount = redisUtil.get(key) != null ? (int) redisUtil.get(key) : 1;
            if (retryCount >= MQConstants.MAX_RECONSUME_COUNT) {
                // 超过重试次数后，放入死信队列
                channel.basicNack(deliverTag, false, false);
                redisUtil.delete(key);
                log.info("OmsOrderConsumer processMsg error:{}, traceId={}", errMsg, MDCUtil.getTraceId());
//                sendException.send(StringUtils.format("OmsOrderConsumer processMsg error:{} \ntraceId={} \nmessageId={}, orderNo={}", errMsg, MDCUtil.getTraceId(), messageId, orderNo));
            } else {
                log.info(">>>>>>>>>>messageId:{}, retryCount:{}", messageId, retryCount);
                // 重新放回队列，进行重试
                channel.basicNack(deliverTag, false, true);
                redisUtil.set(key, retryCount + 1, 5, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            log.error("repositioning to queue failed:", e);
        }
    }



}
