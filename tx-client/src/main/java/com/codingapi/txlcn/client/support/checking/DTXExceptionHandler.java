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
package com.codingapi.txlcn.client.support.checking;

import com.codingapi.txlcn.commons.exception.BeforeBusinessException;
import com.codingapi.txlcn.commons.exception.TxClientException;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
public interface DTXExceptionHandler {


    /**
     * 处理创建事务组TxManager业务异常
     *
     * @param params params
     * @param ex ex
     * @throws BeforeBusinessException BeforeBusinessException
     */
    void handleCreateGroupBusinessException(Object params, Throwable ex) throws BeforeBusinessException;

    /**
     * 处理创建事务组TxManager通信异常
     *
     * @param params params
     * @param ex ex
     * @throws  BeforeBusinessException BeforeBusinessException
     */
    void handleCreateGroupMessageException(Object params, Throwable ex)throws BeforeBusinessException;


    /**
     * 加入事务组TxManager业务异常
     *
     * @param params params
     * @param  ex ex
     * @throws  TxClientException TxClientException
     */
    void handleJoinGroupBusinessException(Object params, Throwable ex) throws TxClientException;

    /**
     * 加入事务组TxManager通讯异常
     *
     * @param params params
     * @param ex ex
     * @throws  TxClientException TxClientException
     */
    void handleJoinGroupMessageException(Object params, Throwable ex) throws TxClientException;

    /**
     * 通知事务组业务异常处理
     *
     * @param params params
     * @param ex ex
     */
    void handleNotifyGroupBusinessException(Object params, Throwable ex);

    /**
     * 通知事务组消息异常处理
     *
     * @param params params
     * @param ex ex
     */
    void handleNotifyGroupMessageException(Object params, Throwable ex);

}
