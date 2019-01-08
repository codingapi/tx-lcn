package com.codingapi.tx.manager.core.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 2018/12/28
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TxManagerInfo {

    /**
     * Netty主机
     */
    private String socketHost;

    /**
     * Netty端口
     */
    private int socketPort;

    /**
     * Netty心跳时间
     */
    private long heartbeatTime;

    /**
     * 注册的资源管理服务数量
     */
    private int clientCount;

    /**
     * 事务并发处理等级
     */
    private int concurrentLevel;

    /**
     * 分布式事务时间
     */
    private long dtxTime;

    /**
     * 异常通知
     */
    private String exUrl;
}
