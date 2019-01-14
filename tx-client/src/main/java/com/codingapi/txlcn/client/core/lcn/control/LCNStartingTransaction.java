package com.codingapi.txlcn.client.core.lcn.control;

import com.codingapi.txlcn.client.bean.DTXLocal;
import com.codingapi.txlcn.client.bean.TxTransactionInfo;
import com.codingapi.txlcn.client.support.TXLCNTransactionControl;
import com.codingapi.txlcn.client.support.common.template.TransactionControlTemplate;
import com.codingapi.txlcn.commons.exception.BeforeBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lorne
 * @date 2018/12/2
 * @description
 */
@Service(value = "control_lcn_starting")
@Slf4j
public class LCNStartingTransaction implements TXLCNTransactionControl {

    private final TransactionControlTemplate transactionControlTemplate;


    @Autowired
    public LCNStartingTransaction(TransactionControlTemplate transactionControlTemplate) {
        this.transactionControlTemplate = transactionControlTemplate;
    }

    @Override
    public void preBusinessCode(TxTransactionInfo info) throws BeforeBusinessException {
        // 创建事务组
        transactionControlTemplate.createGroup(
                info.getGroupId(), info.getUnitId(), info.getTransactionInfo(), info.getTransactionType());

        // LCN 类型事务需要代理资源
        DTXLocal.makeProxy();
    }

    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        DTXLocal.cur().setState(0);
    }

    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) {
        DTXLocal.cur().setState(1);
    }

    @Override
    public void postBusinessCode(TxTransactionInfo info) {
        // RPC 关闭事务组
        transactionControlTemplate.notifyGroup(
                info.getGroupId(), info.getUnitId(), info.getTransactionType(), DTXLocal.cur().getState());
    }
}
