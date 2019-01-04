package com.codingapi.tx.client.spi.transaction.tcc.control;

import com.codingapi.tx.commons.annotation.TccTransaction;
import com.codingapi.tx.client.bean.TCCTransactionInfo;
import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.bean.TxTransactionLocal;
import com.codingapi.tx.client.support.separate.TXLCNTransactionControl;
import com.codingapi.tx.client.support.common.template.TransactionControlTemplate;
import com.codingapi.tx.client.support.common.cache.TransactionAttachmentCache;
import com.codingapi.tx.commons.exception.BeforeBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author 侯存路
 * @date 2018/12/3
 * @company codingApi
 * @description
 */
@Service(value = "control_tcc_starting")
@Slf4j
public class TCCStartingTransaction implements TXLCNTransactionControl {

    private final TransactionAttachmentCache transactionAttachmentCache;

    private final TransactionControlTemplate transactionControlTemplate;

    @Autowired
    public TCCStartingTransaction(TransactionAttachmentCache transactionAttachmentCache,
                                  TransactionControlTemplate transactionControlTemplate) {
        this.transactionAttachmentCache = transactionAttachmentCache;
        this.transactionControlTemplate = transactionControlTemplate;
    }

    @Override
    public void preBusinessCode(TxTransactionInfo info) throws BeforeBusinessException {
        log.info(" TCC  > transaction >  starting ");

        Method method = info.getPointMethod();

        TccTransaction tccTransaction = method.getAnnotation(TccTransaction.class);
        if (tccTransaction == null) {
            throw new IllegalStateException(" TCC模式下需添加 @TccTransaction 注解在  " + method.getName() + " 上 ");
        }
        if (StringUtils.isEmpty(tccTransaction.cancelMethod())) {
            throw new NullPointerException(" @TccTransaction 需指明 回滚执行方法名称！ ");
        }
        if (StringUtils.isEmpty(tccTransaction.confirmMethod())) {
            throw new NullPointerException(" @TccTransaction 需指明 确认执行方法名称！");
        }

        TCCTransactionInfo tccInfo = new TCCTransactionInfo();
        tccInfo.setExecuteClass(tccTransaction.executeClass());
        tccInfo.setCancelMethod(tccTransaction.cancelMethod());
        tccInfo.setConfirmMethod(tccTransaction.confirmMethod());
        tccInfo.setMethodParameter(info.getTransactionInfo().getArgumentValues());
        tccInfo.setMethodTypeParameter(info.getTransactionInfo().getParameterTypes());
        UnitTCCInfoMap unitTCCInfoMap = new UnitTCCInfoMap();
        unitTCCInfoMap.put(info.getUnitId(), tccInfo);
        transactionAttachmentCache.attach(info.getGroupId(), info.getUnitId(), unitTCCInfoMap);

        // 创建事务组
        transactionControlTemplate.createGroup(
                info.getGroupId(), info.getUnitId(), info.getTransactionInfo(), info.getTransactionType());
    }

    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        TxTransactionLocal.current().setState(0);
    }

    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) {
        TxTransactionLocal.current().setState(1);
    }

    /**
     * 事务发起方 自己执行  提交 / 取消 事件
     *
     * @param info
     */
    @Override
    public void postBusinessCode(TxTransactionInfo info) {
        transactionControlTemplate.notifyGroup(
                info.getGroupId(), info.getUnitId(), info.getTransactionType(), TxTransactionLocal.current().getState());
    }
}
