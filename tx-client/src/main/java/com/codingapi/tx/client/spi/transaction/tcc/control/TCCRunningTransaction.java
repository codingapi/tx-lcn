package com.codingapi.tx.client.spi.transaction.tcc.control;

import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.support.separate.TXLCNTransactionControl;
import com.codingapi.tx.client.support.common.template.TransactionCleanTemplate;
import com.codingapi.tx.client.support.common.template.TransactionControlTemplate;
import com.codingapi.tx.client.support.common.cache.TransactionAttachmentCache;
import com.codingapi.tx.commons.exception.BeforeBusinessException;
import com.codingapi.tx.commons.exception.TransactionClearException;
import com.codingapi.tx.commons.exception.TxClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 侯存路
 * @date 2018/12/3
 * @company codingApi
 * @description
 */
@Service(value = "control_tcc_running")
@Slf4j
public class TCCRunningTransaction implements TXLCNTransactionControl {

    private final TransactionAttachmentCache transactionAttachmentCache;

    private final TransactionCleanTemplate transactionCleanTemplate;

    private final TransactionControlTemplate transactionControlTemplate;

    @Autowired
    public TCCRunningTransaction(TransactionAttachmentCache transactionAttachmentCache,
                                 TransactionCleanTemplate transactionCleanTemplate,
                                 TransactionControlTemplate transactionControlTemplate) {
        this.transactionAttachmentCache = transactionAttachmentCache;
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.transactionControlTemplate = transactionControlTemplate;
    }

    @Override
    public void preBusinessCode(TxTransactionInfo info) throws BeforeBusinessException {
        log.info(" TCC  > transaction >  running ");
        String groupId = info.getGroupId();
        UnitTCCInfoMap map = transactionAttachmentCache.attachment(groupId, info.getUnitId(), UnitTCCInfoMap.class, UnitTCCInfoMap::new);
        map.put(info.getUnitId(), TCCStartingTransaction.prepareTccInfo(info));
    }

    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        try {
            transactionCleanTemplate.clean(
                    DTXLocal.cur().getGroupId(),
                    info.getUnitId(),
                    info.getTransactionType(),
                    0);
        } catch (TransactionClearException e) {
            log.error("tcc > clean transaction error.", e);
        }
    }

    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) throws TxClientException {
        transactionControlTemplate.joinGroup(info.getGroupId(), info.getUnitId(), info.getTransactionType(),
                info.getTransactionInfo());
    }

}
