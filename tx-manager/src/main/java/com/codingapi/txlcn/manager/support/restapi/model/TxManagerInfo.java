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
package com.codingapi.txlcn.manager.support.restapi.model;

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
