package com.codingapi.tx.client.support.common.template;

import com.codingapi.tx.client.aspectlog.AspectLogger;
import com.codingapi.tx.client.support.LCNTransactionBeanHelper;
import com.codingapi.tx.client.support.checking.DTXChecking;
import com.codingapi.tx.commons.exception.TransactionClearException;
import com.codingapi.tx.commons.util.Transactions;
import com.codingapi.tx.logger.TxLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: 事务清理模板
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component
@Slf4j
public class TransactionCleanTemplate {

    private final LCNTransactionBeanHelper transactionBeanHelper;

    private final DTXChecking dtxChecking;

    private final AspectLogger aspectLogger;

    private final TxLogger txLogger;

    @Autowired
    public TransactionCleanTemplate(LCNTransactionBeanHelper transactionBeanHelper,
                                    DTXChecking dtxChecking,
                                    AspectLogger aspectLogger,
                                    TxLogger txLogger) {
        this.transactionBeanHelper = transactionBeanHelper;
        this.dtxChecking = dtxChecking;
        this.aspectLogger = aspectLogger;
        this.txLogger = txLogger;
    }

    /**
     * 清理事务
     *
     * @param groupId
     * @param unitId
     * @param unitType
     * @param state
     * @throws TransactionClearException
     */
    public void clean(String groupId, String unitId, String unitType, int state) throws TransactionClearException {
        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "clean transaction");

        transactionBeanHelper.loadTransactionCleanService(unitType).clear(
                groupId, state, unitId, unitType
        );

        dtxChecking.stopDelayChecking(groupId, unitId);

        aspectLogger.clearLog(groupId, unitId);

        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "clean transaction over");

        log.info("clean transaction over");
    }

    /**
     * 清理事务（不清理切面日志）
     *
     * @param groupId
     * @param unitId
     * @param unitType
     * @param state
     * @throws TransactionClearException
     */
    public void compensationClean(String groupId, String unitId, String unitType, int state) throws TransactionClearException {
        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "clean compensation transaction");
        transactionBeanHelper.loadTransactionCleanService(unitType).clear(
                groupId, state, unitId, unitType
        );

        dtxChecking.stopDelayChecking(groupId, unitId);
    }
}
