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
package com.codingapi.txlcn.client.core.txc.resource.init;

import com.codingapi.txlcn.client.core.txc.resource.def.config.TxcConfig;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 * Date: 2018/12/24
 *
 * @author ujued
 */
public class DefaultTxcSettingFactory implements TxcSettingFactory {

    @Autowired
    private TxcConfig txcConfig;

    @Override
    public String lockTableSql() {
        return txcConfig.getLockTableSql();
    }

    @Override
    public String undoLogTableSql() {
        return txcConfig.getUndoLogTableSql();
    }

    @Override
    public boolean enable() {
        return txcConfig.isEnable();
    }

    @Override
    public String lockTableName() {
        return txcConfig.getLockTableName();
    }

    @Override
    public String undoLogTableName() {
        return txcConfig.getUndoLogTableName();
    }
}
