package com.codingapi.tx.client.spi.transaction.lcn.control;

import com.codingapi.tx.commons.util.Transactions;
import com.codingapi.tx.client.springcloud.spi.sleuth.TracerHelper;
import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.support.separate.CustomizableTransactionSeparator;
import com.codingapi.tx.client.support.separate.TXLCNTransactionState;
import com.codingapi.tx.client.support.common.TransactionUnitTypeList;
import com.codingapi.tx.client.support.common.cache.TransactionAttachmentCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Description:
 * Date: 2018/12/5
 *
 * @author ujued
 */
@Slf4j
@Component("transaction_state_resolver_lcn")
public class LCNTypeTransactionSeparator extends CustomizableTransactionSeparator {

    private final TransactionAttachmentCache transactionAttachmentCache;

    private final TracerHelper tracerHelper;

    @Autowired
    public LCNTypeTransactionSeparator(TransactionAttachmentCache transactionAttachmentCache, TracerHelper tracerHelper) {
        this.transactionAttachmentCache = transactionAttachmentCache;
        this.tracerHelper = tracerHelper;
    }

    @Override
    public TXLCNTransactionState loadTransactionState(TxTransactionInfo txTransactionInfo) {

        // 不存在GroupId时不自定义
        if (tracerHelper.getGroupId() == null) {
            return super.loadTransactionState(txTransactionInfo);
        }

        // 一个模块存在多个LCN类型的事务单元在一个事务内走DEFAULT
        Optional<TransactionUnitTypeList> sameTransUnitTypeList =
                transactionAttachmentCache.attachment(tracerHelper.getGroupId(), TransactionUnitTypeList.class);
        if (sameTransUnitTypeList.isPresent() && sameTransUnitTypeList.get().contains(Transactions.LCN)) {
            log.info("Default by LCN assert !");
            return TXLCNTransactionState.DEFAULT;
        }
        return super.loadTransactionState(txTransactionInfo);
    }
}
