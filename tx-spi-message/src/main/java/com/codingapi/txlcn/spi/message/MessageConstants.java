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

/**
 * Description:
 * Date: 2018/12/6
 *
 * @author ujued
 */
public class MessageConstants {

    /**
     * 创建事务组
     */
    public static final String ACTION_CREATE_GROUP = "createGroup";

    /**
     * 加入事务组
     */
    public static final String ACTION_JOIN_GROUP = "joinGroup";

    /**
     * 关闭事务组
     */
    public static final String ACTION_NOTIFY_GROUP = "notifyGroup";

    /**
     * 事务通知
     */
    public static final String ACTION_NOTIFY_UNIT = "notifyUnit";

    /**
     * 询问事务状态
     */
    public static final String ACTION_ASK_TRANSACTION_STATE = "askTransactionState";

    /**
     * 写异常记录
     */
    public static final String ACTION_WRITE_EXCEPTION = "writeException";

    /**
     * 心态检测
     */
    public static final String ACTION_HEART_CHECK = "heartCheck";

    /**
     * 通知建立连接
     */
    public static final String ACTION_NEW_TXMANAGER = "newTxManager";

    /**
     * 获取切面日志
     */
    public static final String ACTION_GET_ASPECT_LOG = "getAspectLog";
    /**
     * 初始化客户端
     */
    public static final String ACTION_INIT_CLIENT = "initClient";




    /**
     * 发起请求状态
     */
    public static final int STATE_REQUEST = 100;

    /**
     * 响应成功状态
     */
    public static final int STATE_OK = 200;

    /**
     * 响应异常状态
     */
    public static final int STATE_EXCEPTION = 500;


}
