package com.codingapi.tx.client.spi.transaction.lcn.control;

import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.commons.exception.TransactionClearException;
import com.codingapi.tx.spi.sleuth.TracerHelper;
import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.support.separate.TXLCNTransactionControl;
import com.codingapi.tx.client.support.common.cache.TransactionAttachmentCache;
import com.codingapi.tx.client.support.common.template.TransactionCleanTemplate;
import com.codingapi.tx.client.support.common.template.TransactionControlTemplate;
import com.codingapi.tx.commons.util.Transactions;
import com.codingapi.tx.commons.exception.TxClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Description:
 * Date: 2018/12/3
 *
 * @author ujued
 */
@Component("control_lcn_running")
@Slf4j
public class LCNRunningTransaction implements TXLCNTransactionControl {

    private final TracerHelper tracerHelper;

    private final TransactionAttachmentCache transactionAttachmentCache;

    private final TransactionCleanTemplate transactionCleanTemplate;

    private final TransactionControlTemplate transactionControlTemplate;

    @Autowired
    public LCNRunningTransaction(TransactionAttachmentCache transactionAttachmentCache,
                                 TracerHelper tracerHelper,
                                 TransactionCleanTemplate transactionCleanTemplate,
                                 TransactionControlTemplate transactionControlTemplate) {
        this.transactionAttachmentCache = transactionAttachmentCache;
        this.tracerHelper = tracerHelper;
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.transactionControlTemplate = transactionControlTemplate;
    }


    @Override
    public void preBusinessCode(TxTransactionInfo info) {
        // LCN 类型事务需要代理资源
        DTXLocal.makeProxy();
    }


    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        try {
            transactionCleanTemplate.clean(info.getGroupId(), info.getUnitId(), info.getTransactionType(), 0);
        } catch (TransactionClearException e) {
            log.error("{} > clean transaction error.", Transactions.LCN);
        }
    }


    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) throws TxClientException {
        log.debug("join group: [GroupId: {}, TxManager:{}, Method: {}]", info.getGroupId(),
                Optional.ofNullable(tracerHelper.getTxManagerKey()).orElseThrow(() -> new RuntimeException("sleuth pass error.")),
                info.getTransactionInfo().getMethodStr());

        // 加入事务组
        transactionControlTemplate.joinGroup(info.getGroupId(), info.getUnitId(), info.getTransactionType(),
                info.getTransactionInfo());
    }

}
