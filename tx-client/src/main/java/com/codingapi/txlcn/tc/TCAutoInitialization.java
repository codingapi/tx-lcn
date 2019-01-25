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
package com.codingapi.txlcn.tc;

import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
import com.codingapi.txlcn.spi.message.RpcConfig;
import com.codingapi.txlcn.tc.corelog.aspect.AspectLogHelper;
import com.codingapi.txlcn.tc.corelog.txc.TxcLogHelper;
import com.codingapi.txlcn.tc.message.TXLCNClientMessageServer;
import com.codingapi.txlcn.tc.support.checking.DTXChecking;
import com.codingapi.txlcn.tc.support.checking.SimpleDTXChecking;
import com.codingapi.txlcn.tc.support.template.TransactionCleanTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/16
 *
 * @author codingapi
 */
@Component
public class TCAutoInitialization implements TxLcnInitializer {

    private final AspectLogHelper aspectLogHelper;

    private final TxcLogHelper txcLogHelper;

    private final TXLCNClientMessageServer txLcnClientMessageServer;

    private final DTXChecking dtxChecking;

    private final TransactionCleanTemplate transactionCleanTemplate;

    private final RpcConfig rpcConfig;

    @Autowired
    public TCAutoInitialization(AspectLogHelper aspectLogHelper,
                                TXLCNClientMessageServer txLcnClientMessageServer,
                                DTXChecking dtxChecking,
                                TransactionCleanTemplate transactionCleanTemplate, TxcLogHelper txcLogHelper,
                                RpcConfig rpcConfig) {
        this.aspectLogHelper = aspectLogHelper;
        this.txLcnClientMessageServer = txLcnClientMessageServer;
        this.dtxChecking = dtxChecking;
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.txcLogHelper = txcLogHelper;
        this.rpcConfig = rpcConfig;
    }

    @Override
    public void init() throws Exception {
        // aspect log init (H2db).
        aspectLogHelper.init();

        // txc undo log init (H2db).
        txcLogHelper.init();

        // rpc env init
        rpcEnvInit();

        // aware the transaction clean template to the simpleDtxChecking
        dtxCheckingTransactionCleanTemplateAdapt();
    }

    @Override
    public int order() {
        return -1;
    }

    private void dtxCheckingTransactionCleanTemplateAdapt() {
        if (dtxChecking instanceof SimpleDTXChecking) {
            ((SimpleDTXChecking) dtxChecking).setTransactionCleanTemplate(transactionCleanTemplate);
        }
    }

    private void rpcEnvInit() throws Exception {
        if (rpcConfig.getWaitTime() == -1) {
            rpcConfig.setWaitTime(2000);
        }

        // rpc client init.
        txLcnClientMessageServer.init();
    }
}
