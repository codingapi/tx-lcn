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
package com.codingapi.txlcn.manager.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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

    public TxManagerConfig(@Value("${server.port}") Integer port) {
        this.port = port + 1;
        this.host = "127.0.0.1";
        this.heartTime = 5;
        this.concurrentLevel = 0;
        this.dtxTime = 36000;
        this.adminKey = "codingapi";
        this.exUrl = "/provider/email-to/ujued@qq.com";
    }

    /**
     * manager host
     */
    private String host;

    /**
     * support  port
     */
    private int port;

    /**
     * netty heart check time (s)
     */
    private int heartTime;

    /**
     * 事务处理并发等级
     */
    private int concurrentLevel;

    /**
     * 分布式事务超时时间
     */
    private int dtxTime;

    /**
     * 后台密码
     */
    private String adminKey;

    /**
     * 是否允许异常回调
     */
    private boolean exUrlEnabled = true;

    /**
     * 异常回调地址
     */
    private String exUrl;
}
