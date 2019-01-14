package com.codingapi.tx.client.spi.transaction.txc.control;

import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.spi.transaction.txc.resource.def.TxcService;
import com.codingapi.tx.client.support.message.TxMangerReporter;
import com.codingapi.tx.client.support.separate.TXLCNTransactionControl;
import com.codingapi.tx.client.support.common.template.TransactionCleanTemplate;
import com.codingapi.tx.client.support.common.template.TransactionControlTemplate;
import com.codingapi.tx.client.spi.transaction.txc.resource.def.bean.RollbackInfo;
import com.codingapi.tx.commons.exception.TransactionClearException;
import com.codingapi.tx.commons.exception.TxClientException;
import com.codingapi.tx.commons.exception.TxcLogicException;
import com.codingapi.tx.logger.TxLogger;
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
    private TxMangerReporter txMangerReporter;

    @Autowired
    private TxLogger txLogger;

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
        try {
            txcService.writeUndoLog(
                    info.getGroupId(), info.getUnitId(), (RollbackInfo) DTXLocal.cur().getAttachment());
        } catch (TxcLogicException e) {
            //执行回滚处理。（启用备用连接池的方式）
            try {
                RollbackInfo rollbackInfo = (RollbackInfo) DTXLocal.cur().getAttachment();
                txLogger.trace(info.getGroupId(), info.getUnitId(), "txc", "exception rollbackInfo sql " + rollbackInfo.toString());
                txcService.undoRollbackInfo(rollbackInfo);
            } catch (TxcLogicException e1) {
                txMangerReporter.reportTxcRollbackException(info.getGroupId(), info.getUnitId());
            }
            throw new TxClientException("txc rollback fail.");
        }
        // 加入事务组
        transactionControlTemplate.joinGroup(info.getGroupId(), info.getUnitId(), info.getTransactionType(),
                info.getTransactionInfo());
    }
}
