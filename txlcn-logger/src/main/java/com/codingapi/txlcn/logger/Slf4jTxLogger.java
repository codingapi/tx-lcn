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
package com.codingapi.txlcn.logger;

import com.codingapi.txlcn.common.util.Transactions;
import lombok.extern.slf4j.Slf4j;

/**
 * 不开启日志输出的时候 空操作
 *
 * @author meetzy
 */
@Slf4j
public class Slf4jTxLogger implements TxLogger {

    @Override
    public void trace(String groupId, String unitId, String tag, String content, Object... args) {
        log.debug("{}-{}-{}: " + content, groupId, unitId, tag, content, args);
    }

    @Override
    public void info(String groupId, String unitId, String tag, String content, Object... args) {
        log.debug("{}-{}-{}: " + content, groupId, unitId, tag, args);
    }

    @Override
    public void error(String groupId, String unitId, String content, Object... args) {
        log.error("{}-{}-{}: " + content, groupId, unitId, Transactions.TE, args);
    }
}