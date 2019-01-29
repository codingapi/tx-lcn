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

import com.codingapi.txlcn.logger.db.TxLog;
import com.codingapi.txlcn.logger.helper.TxLcnLogDbHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/26
 *
 * @author codingapi
 */
@Slf4j
public class DefaultTxLogger extends AbstractTxLogger {

    private final TxLcnLogDbHelper txLcnLogDbHelper;

    public DefaultTxLogger(TxLcnLogDbHelper txLcnLogDbHelper) {
        this.txLcnLogDbHelper = txLcnLogDbHelper;
    }


    @Override
    public void saveTrace(TxLog txLog) {
        txLcnLogDbHelper.insert(txLog);
    }
}
