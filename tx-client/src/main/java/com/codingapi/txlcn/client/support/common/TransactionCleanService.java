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
package com.codingapi.txlcn.client.support.common;

import com.codingapi.txlcn.commons.exception.TransactionClearException;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
public interface TransactionCleanService {

    /**
     * 事务清理业务
     *
     * @param groupId groupId
     * @param state    事务状态 1 提交 0 回滚
     * @param unitId unitId
     * @param unitType 事务类型
     * @throws TransactionClearException TransactionClearException
     */
    void clear(String groupId, int state, String unitId, String unitType) throws TransactionClearException;
}
