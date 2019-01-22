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

import com.codingapi.txlcn.tc.corelog.aspect.AspectLogHelper;
import com.codingapi.txlcn.tc.corelog.txc.TxcLogHelper;
import com.codingapi.txlcn.tc.message.TXLCNClientMessageServer;
import com.codingapi.txlcn.tc.support.checking.DTXChecking;
import com.codingapi.txlcn.tc.support.checking.SimpleDTXChecking;
import com.codingapi.txlcn.tc.support.template.TransactionCleanTemplate;
import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
import com.codingapi.txlcn.commons.util.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/16
 *
 * @author codingapi
 */
@Component
public class TCInitialization implements TxLcnInitializer {

    private final AspectLogHelper aspectLogHelper;

    private final TxcLogHelper txcLogHelper;

    private final TXLCNClientMessageServer txLcnClientMessageServer;

    private final DTXChecking dtxChecking;

    private final TransactionCleanTemplate transactionCleanTemplate;

    private final ConfigurableEnvironment environment;

    @Autowired
    public TCInitialization(AspectLogHelper aspectLogHelper,
                            TXLCNClientMessageServer txLcnClientMessageServer,
                            DTXChecking dtxChecking,
                            TransactionCleanTemplate transactionCleanTemplate, TxcLogHelper txcLogHelper,
                            ConfigurableEnvironment environment) {
        this.aspectLogHelper = aspectLogHelper;
        this.txLcnClientMessageServer = txLcnClientMessageServer;
        this.dtxChecking = dtxChecking;
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.txcLogHelper = txcLogHelper;
        this.environment = environment;
    }

    @Override
    public void init() throws Exception {
        aspectLogHelper.init();

        txcLogHelper.init();

        txLcnClientMessageServer.init();

        // aware the transaction clean template to the simpleDtxChecking
        dtxCheckingTransactionCleanTemplateAdapt();

        // init util classes
        utilClassesInit();
    }

    private void dtxCheckingTransactionCleanTemplateAdapt() {
        if (dtxChecking instanceof SimpleDTXChecking) {
            ((SimpleDTXChecking) dtxChecking).setTransactionCleanTemplate(transactionCleanTemplate);
        }
    }

    private void utilClassesInit() {
        String name = environment.getProperty("spring.application.name");
        String application = StringUtils.hasText(name) ? name : "application";
        String port = environment.getProperty("server.port");
        Transactions.setApplicationIdWhenRunning(String.format("%s:%s", application, port));
    }
}
