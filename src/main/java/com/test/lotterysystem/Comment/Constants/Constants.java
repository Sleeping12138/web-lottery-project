package com.test.lotterysystem.Comment.Constants;

public class Constants {
    /**
     * 普通队列
     */
    public static final String QUEUE_NAME = "DirectQueue";
    public static final String EXCHANGE_NAME = "DirectExchange";
    public static final String KEY = "DirectRouting";


    /**
     * 死信队列
     */
    public static final String DLX_QUEUE_NAME = "DlxDirectQueue";
    public static final String DLX_EXCHANGE_NAME = "DlxDirectExchange";
    public static final String DLX_KEY = "DlxDirectRouting";
}
