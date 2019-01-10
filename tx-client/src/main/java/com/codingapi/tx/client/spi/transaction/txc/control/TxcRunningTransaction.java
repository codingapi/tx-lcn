package com.codingapi.tx.client.spi.transaction.txc.control;

import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.spi.transaction.txc.resource.def.TxcService;
import com.codingapi.tx.client.support.separate.TXLCNTransactionControl;
import com.codingapi.tx.client.support.common.template.TransactionCleanTemplate;
import com.codingapi.tx.client.support.common.template.TransactionControlTemplate;
import com.codingapi.tx.client.spi.transaction.txc.resource.def.bean.RollbackInfo;
import com.codingapi.tx.commons.exception.TransactionClearException;
import com.codingapi.tx.commons.exception.TxClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component("control_txc_running")
@Slf4j
public class TxcRunningTransaction implements TXLCNTransactionControl {

    private final TxcService txcService;

    private final TransactionCleanTemplate transactionCleanTemplate;

    private final TransactionControlTemplate transactionControlTemplate;


    @Autowired
    public TxcRunningTransaction(TxcService txcService,
                                 TransactionCleanTemplate transactionCleanTemplate,
                                 TransactionControlTemplate transactionControlTemplate) {
        this.txcService = txcService;
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.transactionControlTemplate = transactionControlTemplate;
    }

    @Override
    public void preBusinessCode(TxTransactionInfo info) {

        // 准备回滚信息容器
        DTXLocal.cur().setAttachment(new RollbackInfo());

        // TXC 类型事务需要代理资源
        DTXLocal.makeProxy();
    }

    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        // 写Undo log 早于 clean
        txcService.writeUndoLog(
                info.getGroupId(), info.getUnitId(), (RollbackInfo) DTXLocal.cur().getAttachment());

        try {
            log.info("txc > running > clean transaction.");
            transactionCleanTemplate.clean(
                    DTXLocal.cur().getGroupId(),
                    info.getUnitId(),
                    info.getTransactionType(),
                    0);
        } catch (TransactionClearException e) {
            log.error("txc > Clean Transaction Error", e);
        }
    }

    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) throws TxClientException {
        // 写Undo log
        txcService.writeUndoLog(
                info.getGroupId(), info.getUnitId(), (RollbackInfo) DTXLocal.cur().getAttachment());
        // 加入事务组
        transactionControlTemplate.joinGroup(info.getGroupId(), info.getUnitId(), info.getTransactionType(),
                info.getTransactionInfo());
    }
}
