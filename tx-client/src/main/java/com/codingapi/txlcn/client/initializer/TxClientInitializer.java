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
package com.codingapi.txlcn.client.initializer;

import com.codingapi.txlcn.client.aspectlog.AspectLogHelper;
import com.codingapi.txlcn.client.message.TXLCNClientMessageServer;
import com.codingapi.txlcn.client.support.checking.DTXChecking;
import com.codingapi.txlcn.client.support.checking.SimpleDTXChecking;
import com.codingapi.txlcn.client.support.template.TransactionCleanTemplate;
import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
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
public class TxClientInitializer implements TxLcnInitializer {

    private final AspectLogHelper aspectLogHelper;

    private final TXLCNClientMessageServer txLcnClientMessageServer;

    private final DTXChecking dtxChecking;

    private final TransactionCleanTemplate transactionCleanTemplate;

    @Autowired
    public TxClientInitializer(AspectLogHelper aspectLogHelper,
                               TXLCNClientMessageServer txLcnClientMessageServer,
                               DTXChecking dtxChecking,
                               TransactionCleanTemplate transactionCleanTemplate) {
        this.aspectLogHelper = aspectLogHelper;
        this.txLcnClientMessageServer = txLcnClientMessageServer;
        this.dtxChecking = dtxChecking;
        this.transactionCleanTemplate = transactionCleanTemplate;
    }

    @Override
    public void init() throws Exception {
        aspectLogHelper.init();
        txLcnClientMessageServer.init();

        // aware the clean template to the simpleDtxChecking
        dtxCheckingTransactionCleanTemplateAdapt();
    }

    private void dtxCheckingTransactionCleanTemplateAdapt() {
        if (dtxChecking instanceof SimpleDTXChecking) {
            ((SimpleDTXChecking) dtxChecking).setTransactionCleanTemplate(transactionCleanTemplate);
        }
    }
}
