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
package com.codingapi.txlcn.client.aspectlog;

import com.codingapi.txlcn.commons.bean.TransactionInfo;

/**
 * Description: 切面日志操作
 * Date: 2018/12/28
 *
 * @author ujued
 */
public interface AspectLogger {

    /**
     * 记录切面日志
     *
     * @param groupId groupId
     * @param unitId unitId
     * @param transactionInfo 切面信息
     */
    void trace(String groupId, String unitId, TransactionInfo transactionInfo);

    /**
     * 清理切面日志
     *
     * @param groupId groupId
     * @param unitId unitId
     */
    void clearLog(String groupId, String unitId);
}
