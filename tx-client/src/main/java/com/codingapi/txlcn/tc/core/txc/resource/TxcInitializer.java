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
package com.codingapi.txlcn.tc.core.txc.resource;

import com.codingapi.txlcn.tc.core.txc.resource.def.TxcSqlExecutor;
import com.codingapi.txlcn.tc.core.txc.resource.init.TxcSql;
import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/17
 *
 * @author codingapi
 */
@Component
public class TxcInitializer implements TxLcnInitializer {


    @Autowired
    private TableStructAnalyser tableStructAnalyser;

    @Autowired
    private TxcSql txcSql;

    @Autowired
    private TxcSqlExecutor txcSqlExecutor;


    @Override
    public void init() throws Exception {
        if (!tableStructAnalyser.existsTable(txcSql.lockTableName())) {
            txcSqlExecutor.createLockTable();
        }
        if (!tableStructAnalyser.existsTable(txcSql.undoLogTableName())) {
            txcSqlExecutor.createUndoLogTable();
        }
    }
}
