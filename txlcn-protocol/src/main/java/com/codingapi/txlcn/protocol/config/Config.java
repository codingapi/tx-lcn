package com.codingapi.txlcn.protocol.config;

import lombok.Data;
import lombok.ToString;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@Data
@ToString
public class Config {

    /**
     * 监听端口
     */
    private int port = 8888;

    /**
     * 心跳检测-最大读取超时时间
     */
    private int maxReadIdleSeconds = 60;

    /**
     * 心跳检测-最大写入超时时间
     */
    private int maxWriteIdleSeconds = 20;

    /**
     * handle机制最大线程数
     */
    private int handleThreads = 30;

    /**
     * 请求发送消息的等待时间(单位:毫秒)
     */
    private int awaitTime = 1000;


}
