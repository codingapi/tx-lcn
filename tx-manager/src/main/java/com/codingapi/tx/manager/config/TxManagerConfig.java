package com.codingapi.tx.manager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/11/29
 *
 * @author ujued
 */
@ConfigurationProperties(prefix = "tx-lcn.manager")
@Component
@Data
public class TxManagerConfig {

    /**
     * manager managerHost
     */
    private String managerHost;

    /**
     * support  rpcPort
     */
    private int rpcPort;

    /**
     * netty heart check time (s)
     */
    private int heartTime = 5;

    /**
     * 事务处理并发等级
     */
    private int concurrentLevel;

    /**
     * 分布式事务超时时间
     */
    private int dtxTime = 30000;

    /**
     * 后台密码
     */
    private String adminKey = "codingapi";
}
