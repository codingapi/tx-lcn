package com.codingapi.tx.client.spi.transaction.txc.control;

import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.bean.TxTransactionLocal;
import com.codingapi.tx.client.spi.transaction.txc.resource.sql.def.TxcService;
import com.codingapi.tx.client.spi.transaction.txc.resource.sql.def.bean.RollbackInfo;
import com.codingapi.tx.client.support.separate.TXLCNTransactionControl;
import com.codingapi.tx.client.support.common.template.TransactionControlTemplate;
import com.codingapi.tx.commons.exception.BeforeBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component("control_txc_starting")
@Slf4j
public class TxcStartingTransaction implements TXLCNTransactionControl {

    private final TxcService txcService;

    private final TransactionControlTemplate transactionControlTemplate;

    @Autowired
    public TxcStartingTransaction(
            TxcService txcService,
            TransactionControlTemplate transactionControlTemplate) {
        this.txcService = txcService;
        this.transactionControlTemplate = transactionControlTemplate;
    }

    @Override
    public void preBusinessCode(TxTransactionInfo info) throws BeforeBusinessException {
        try {
            //创建事务组
            transactionControlTemplate.createGroup(
                    info.getGroupId(), info.getUnitId(), info.getTransactionInfo(), info.getTransactionType());
        } catch (Exception e) {
            throw new BeforeBusinessException(e);
        }
        // 准备回滚信息容器
        TxTransactionLocal.current().setAttachment(new RollbackInfo());

        // TXC 类型事务需要代理资源
        TxTransactionLocal.makeProxy();

    }

    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        TxTransactionLocal.current().setState(0);

    }

    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) {
        // set state equ 1
        TxTransactionLocal.current().setState(1);
    }

    @Override
    public void postBusinessCode(TxTransactionInfo info) {
        RollbackInfo rollbackInfo = (RollbackInfo) TxTransactionLocal.current().getAttachment();
        int state = TxTransactionLocal.current().getState();

        // 非成功状态。（事务导致）{#link TxcServiceImpl.lockResource}
        if (Objects.nonNull(rollbackInfo) && rollbackInfo.getStatus() < 0) {
            state = -1;
        }

        // 非提交状态，写Undo log
        if (state != 1) {
            txcService.writeUndoLog(info.getGroupId(), info.getUnitId(), rollbackInfo);
        }

        // 关闭事务组
        transactionControlTemplate.notifyGroup(info.getGroupId(), info.getUnitId(), info.getTransactionType(), state);
    }
}
