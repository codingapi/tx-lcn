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

/**
 * Description:分布式事务检测器。未收到通知事务单元指令的超时处理机制
 * Date: 2018/12/19
 *
 * @author ujued
 */
public interface DTXChecking {

    /**
     * 开始事务检测。设置定时器，在超时时间后做最后事务状态的确认
     *
     * @param groupId groupId
     * @param unitId unitId
     * @param transactionType transactionType
     */
    void startDelayCheckingAsync(String groupId, String unitId, String transactionType);

    /**
     * 手动停止事务检测。确定分布式事务结果正常时手动结束检测
     *
     * @param groupId groupId
     * @param unitId unitId
     */
    void stopDelayChecking(String groupId, String unitId);
}
