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
package com.codingapi.txlcn.spi.message;


import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 */
@Slf4j
public enum LCNCmdType {

    /**
     * 事务提交
     */
    notifyUnit("notify-unit", MessageConstants.ACTION_NOTIFY_UNIT),

    /**
     * 创建事务组
     * <p>
     * 简写 cg
     */
    createGroup("create-group", MessageConstants.ACTION_CREATE_GROUP),

    /**
     * 加入事务组
     * <p>
     * 简写 cg
     */
    joinGroup("join-group", MessageConstants.ACTION_JOIN_GROUP),

    /**
     * 通知事务组
     * 简写 clg
     */
    notifyGroup("notify-group", MessageConstants.ACTION_NOTIFY_GROUP),

    /**
     * 响应事务状态
     * 间写 ats
     */
    askTransactionState("ask-transaction-state", MessageConstants.ACTION_ASK_TRANSACTION_STATE),

    /**
     * 记录补偿
     * 简写 wc
     */
    writeCompensation("write-compensation", MessageConstants.ACTION_WRITE_EXCEPTION),


    /**
     * TxManager请求连接
     * 简写 nc
     */
    notifyConnect("notify-connect", MessageConstants.ACTION_NEW_TXMANAGER),


    /**
     * 初始化客户端
     * 简写 ic
     */
    initClient("init-client", MessageConstants.ACTION_INIT_CLIENT),

    /**
     * 获取切面日志
     * 简写 gal
     */
    getAspectLog("get-aspect-log", MessageConstants.ACTION_GET_ASPECT_LOG);


    private String code;

    private String name;

    LCNCmdType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static LCNCmdType parserCmd(String cmd) {
        log.debug("parsed cmd: {}", cmd);
        switch (cmd) {
            case MessageConstants.ACTION_CREATE_GROUP:
                return createGroup;
            case MessageConstants.ACTION_NOTIFY_GROUP:
                return notifyGroup;
            case MessageConstants.ACTION_NOTIFY_UNIT:
                return notifyUnit;
            case MessageConstants.ACTION_JOIN_GROUP:
                return joinGroup;
            case MessageConstants.ACTION_ASK_TRANSACTION_STATE:
                return askTransactionState;
            case MessageConstants.ACTION_WRITE_EXCEPTION:
                return writeCompensation;
            case MessageConstants.ACTION_NEW_TXMANAGER:
                return notifyConnect;
            case MessageConstants.ACTION_GET_ASPECT_LOG:
                return getAspectLog;
            case MessageConstants.ACTION_INIT_CLIENT:
                return initClient;
            default:
                throw new IllegalStateException("unsupported cmd.");
        }
    }
}
