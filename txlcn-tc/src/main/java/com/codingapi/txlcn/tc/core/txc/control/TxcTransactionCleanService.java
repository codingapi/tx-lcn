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
package com.codingapi.txlcn.tc.core.txc.control;

import com.codingapi.txlcn.tc.core.txc.analy.def.TxcService;
import com.codingapi.txlcn.tc.core.TransactionCleanService;
import com.codingapi.txlcn.commons.exception.TransactionClearException;
import com.codingapi.txlcn.commons.exception.TxcLogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component
@Slf4j
public class TxcTransactionCleanService implements TransactionCleanService {

    private final TxcService txcService;

    @Autowired
    public TxcTransactionCleanService(TxcService txcService) {
        this.txcService = txcService;
    }

    @Override
    public void clear(String groupId, int state, String unitId, String unitType) throws TransactionClearException {
        try {
            // 若需要回滚读undo_log，进行回滚
            if (state == 0) {
                txcService.undo(groupId, unitId);
            }

            // 清理TXC
            txcService.cleanTxc(groupId, unitId);
        } catch (TxcLogicException e) {
            throw new TransactionClearException(e);
        }
    }
}
