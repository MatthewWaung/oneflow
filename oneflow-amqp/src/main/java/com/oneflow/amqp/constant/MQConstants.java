package com.oneflow.amqp.constant;

public class MQConstants {

    /**
     * 最大重试次数
     */
    public final static int MAX_RECONSUME_COUNT = 2;

    public static final String BASE_KEY = "oneflow:amqp:";

    public static final String RETRY = BASE_KEY + "retry:";

    public static final String ORDER_EXCHANGE = "exchange_order";
    public static final String ORDER_QUEUE = "queue_order";
    public static final String ORDER_ROUTING_KEY = "order.oms";


    public static final String ORDER_DEAD_LETTER_EXCHANGE = "exchange_oms_deadLetter";
    public static final String ORDER_DEAD_LETTER_QUEUE = "queue_oms_deadLetter";
    public static final String ORDER_DEAD_LETTER_ROUTING_KEY = "dlx.order.oms";
}
