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

import com.codingapi.txlcn.common.util.SpringUtils;
import com.codingapi.txlcn.logger.db.TxLog;
import com.codingapi.txlcn.logger.helper.TxLcnLogDbHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/26
 *
 * @author codingapi
 */
@Slf4j
public class DefaultTxLogger extends AbstractTxLogger {

    private TxLcnLogDbHelper txLcnLogDbHelper;

    private boolean needAware = true;

    public DefaultTxLogger(Class<?> className) {
        super(className);
    }

    public void setTxLcnLogDbHelper(TxLcnLogDbHelper txLcnLogDbHelper) {
        this.txLcnLogDbHelper = txLcnLogDbHelper;
    }

    @Override
    public void saveLog(TxLog txLog) {
        if (needAware && Objects.isNull(txLcnLogDbHelper)) {
            txLcnLogDbHelper = SpringUtils.getBean(TxLcnLogDbHelper.class);
            needAware = false;
        }
        if (Objects.nonNull(txLcnLogDbHelper)) {
            txLcnLogDbHelper.insert(txLog);
            return;
        }
        log.warn("tx-logger db configure fail.");
    }
}
