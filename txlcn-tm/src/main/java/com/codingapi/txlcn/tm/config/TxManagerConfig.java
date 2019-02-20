/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.tm.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

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

    public static final int PORT_CHANGE_VALUE = 100;

    @Autowired
    public TxManagerConfig(ServerProperties serverProperties) {
        this.port = Objects.requireNonNull(serverProperties.getPort(), "TM http port not configured?") +
                PORT_CHANGE_VALUE;
    }

    /**
     * manager host
     */
    private String host = "127.0.0.1";

    /**
     * support  port
     */
    private int port;

    /**
     * netty heart check time (ms)
     */
    private long heartTime = 5 * 60 * 1000;

    /**
     * 事务处理并发等级
     */
    private int concurrentLevel;

    /**
     * 分布式事务锁超时时间
     */
    private long dtxLockTime = -1;

    /**
     * 分布式事务超时时间(ms)
     */
    private long dtxTime = 8 * 1000;

    /**
     * 后台密码
     */
    private String adminKey = "codingapi";

    /**
     * 是否允许异常回调
     */
    private boolean exUrlEnabled = true;

    /**
     * 异常回调地址
     */
    private String exUrl = "/provider/email-to/ujued@qq.com";

    /**
     * ID序列长度
     */
    private int seqLen = 12;

    private long machineId;

    private void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public void applyMachineId(long machineId) {
        setMachineId(machineId);
    }

    public long getDtxLockTime() {
        return dtxLockTime == -1 ? dtxTime : dtxLockTime;
    }
}
