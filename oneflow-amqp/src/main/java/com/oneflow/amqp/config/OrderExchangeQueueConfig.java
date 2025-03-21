package com.oneflow.amqp.config;

import com.oneflow.amqp.constant.MQConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderExchangeQueueConfig {

    // 创建 Direct Exchange
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(MQConstants.ORDER_EXCHANGE,
                true,  // durable: 是否持久化
                false);  // exclusive: 是否排它
    }

    // 创建正常的订单队列，并绑定死信交换机
    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(MQConstants.ORDER_QUEUE) // 队列名称
                .withArgument("x-dead-letter-exchange", MQConstants.ORDER_DEAD_LETTER_EXCHANGE) // 指定死信交换机
                .withArgument("x-dead-letter-routing-key", MQConstants.ORDER_DEAD_LETTER_ROUTING_KEY) // 指定死信队列的路由键
                .withArgument("x-max-length", 10000) // 最大长度，超过这个长度消息进入死信队列（可选）
                .build();
    }

    // 创建 Binding
    @Bean
    public Binding omsOrderPushQianYiBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(MQConstants.ORDER_ROUTING_KEY);
    }


    // ======= OMS死信队列相关配置 ======

    /**
     * OMS死信交换机
     */
    @Bean
    public DirectExchange orderDlxExchange() {
        return new DirectExchange(MQConstants.ORDER_DEAD_LETTER_EXCHANGE, true, false);
    }

    /**
     * OMS死信队列
     */
    @Bean
    public Queue orderDlxQueue() {
        return new Queue(MQConstants.ORDER_DEAD_LETTER_QUEUE, true);
    }

    /**
     * OMS死信队列绑定
     */
    @Bean
    public Binding bindingDeadLetter() {
        return BindingBuilder.bind(orderDlxQueue()).to(orderDlxExchange()).with(MQConstants.ORDER_DEAD_LETTER_ROUTING_KEY);
    }


}
