package com.codingapi.tm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * create by lorne on 2017/11/11
 */
@Component
public class ConfigReader {

    @Value("${tm.socket.port}")
    private int socketPort;

    @Value("${tm.socket.max.connection}")
    private int socketMaxConnection;

    @Value("${tm.transaction.netty.hearttime}")
    private int transactionNettyHeartTime;

    @Value("${tm.transaction.netty.delaytime}")
    private int transactionNettyDelayTime;

    @Value("${tm.redis.savemaxtime}")
    private int redisSaveMaxTime;

    @Value("${tm.compensate.log.path}")
    private String compensateLogPath;

    @Value("${tm.compensate.notify.url}")
    private String compensateNotifyUrl;


    private final String key_prefix = "tx_manager_default_";

    private final String key_prefix_notify = "tx_manager_notify_";


    public String getCompensateLogPath() {
        return compensateLogPath;
    }

    public String getCompensateNotifyUrl() {
        return compensateNotifyUrl;
    }

    public String getKeyPrefix() {
        return key_prefix;
    }

    public String getKeyPrefixNotify() {
        return key_prefix_notify;
    }

    public int getSocketPort(){
        return socketPort;
    }

    public int getSocketMaxConnection() {
        return socketMaxConnection;
    }

    public int getTransactionNettyHeartTime() {
        return transactionNettyHeartTime;
    }


    public int getRedisSaveMaxTime() {
        return redisSaveMaxTime;
    }

    public int getTransactionNettyDelayTime() {
        return transactionNettyDelayTime;
    }
}
