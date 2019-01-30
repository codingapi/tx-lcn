package com.codingapi.txlcn.tc.core.checking;

import com.codingapi.txlcn.common.runner.TxLcnInitializer;
import com.codingapi.txlcn.tc.core.template.TransactionCleanTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 1/27/19
 *
 * @author ujued
 */
@Component
public class DTXCheckingInitialization implements TxLcnInitializer {

    private final DTXChecking dtxChecking;

    private final TransactionCleanTemplate transactionCleanTemplate;

    @Autowired
    public DTXCheckingInitialization(DTXChecking dtxChecking, TransactionCleanTemplate transactionCleanTemplate) {
        this.dtxChecking = dtxChecking;
        this.transactionCleanTemplate = transactionCleanTemplate;
    }

    @Override
    public void init() throws Exception {
        if (dtxChecking instanceof SimpleDTXChecking) {
            ((SimpleDTXChecking) dtxChecking).setTransactionCleanTemplate(transactionCleanTemplate);
        }
    }
}
