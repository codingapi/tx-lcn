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
package com.codingapi.txlcn.manager.core.context;

import org.springframework.stereotype.Component;

/**
 * Description: Transaction只存有GroupId, 目前Redis事务组信息只存groupId
 * 这里没有跟Redis交互, 考虑到后续功能增加, 上下文会由Redis缓存担任
 * Date: 1/11/19
 *
 * @author ujued
 */
@Component
public class DTXTransactionContext {

    public DTXTransaction newContext(String groupId) {
        return getTransaction(groupId);
    }

    public DTXTransaction getTransaction(String groupId) {
        return new SimpleDTXTransaction(groupId);
    }

    void destroyTransaction(String groupId) {
        // noting to do
    }
}
