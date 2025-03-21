package com.oneflow.amqp.consumer;

import com.oneflow.amqp.feign.OmsFeignClient;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OrderMessageConsumer extends AbstractMessageConsumer<Long> {

    @Resource
    private OmsFeignClient omsFeignClient;

    @RabbitListener(queues = "order", concurrency = "2-10")
    public void onMessage(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        Long orderId = Long.parseLong(msg);
//        MsgBody msgBody = JSONObject.parseObject(msgJson, MsgBody.class);
//
//        JSONObject orderJson = msgBody.getMsgInfo();
//        Long orderId = orderJson.getString("orderId");
        log.info("OrderMessageConsumer: {}", orderId);

        // 调用抽象类父类中的方法进行消息处理
        this.handleMessage(orderId, message, channel);

    }

    @RabbitListener(queues = "order.dead", concurrency = "2-10")
    public void onDeadMessage(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        Long orderId = Long.parseLong(msg);
        log.info("OrderMessageConsumer: {}", orderId);

        // 调用抽象类父类中的方法进行消息处理
        this.handleDeadLetterMessage(orderId, message, channel);
    }

    @Override
    public void processMessage(Long orderId) {
        try {
            R result = omsFeignClient.handleOrders(orderId);
            if (null == result || result.getCode() != 200) {
                log.info("OrderMessageConsumer processMessage error result:{}, orderId:{}", result, orderId);
                throw new Exception(result != null ? result.getMsg() : null);
            }
        } catch (Exception e) {
            log.error("调用oms接口失败，orderId: {}", orderId, e);
            throw new RuntimeException(e.getMessage());

//            this.processException(orderId, e);
        }
    }

    @Override
    protected void processDeadLetterMessage(Long orderId) {

        processMessage(orderId);

    }

    @Override
    protected void processException(Long messageObject, Exception e) {

    }

}
