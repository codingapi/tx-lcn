package com.codingapi.tx.client.core.tcc.control;

import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.commons.annotation.TccTransaction;
import com.codingapi.tx.client.bean.TCCTransactionInfo;
import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.support.TXLCNTransactionControl;
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

    static TCCTransactionInfo prepareTccInfo(TxTransactionInfo info) throws BeforeBusinessException {
        Method method = info.getPointMethod();
        TccTransaction tccTransaction = method.getAnnotation(TccTransaction.class);
        if (tccTransaction == null) {
            throw new BeforeBusinessException("TCC模式下需添加 @TccTransaction 注解在 " + method.getName() + " 上 ");
        }
        String cancelMethod = tccTransaction.cancelMethod();
        String confirmMethod = tccTransaction.confirmMethod();
        Class<?> executeClass = tccTransaction.executeClass();
        if (StringUtils.isEmpty(tccTransaction.cancelMethod())) {
            cancelMethod = "cancel" + StringUtils.capitalize(method.getName());
        }
        if (StringUtils.isEmpty(tccTransaction.confirmMethod())) {
            confirmMethod = "confirm" + StringUtils.capitalize(method.getName());
        }
        if (Void.class.isAssignableFrom(executeClass)) {
            executeClass = info.getTransactionInfo().getTargetClazz();
        }

        TCCTransactionInfo tccInfo = new TCCTransactionInfo();
        tccInfo.setExecuteClass(executeClass);
        tccInfo.setCancelMethod(cancelMethod);
        tccInfo.setConfirmMethod(confirmMethod);
        tccInfo.setMethodParameter(info.getTransactionInfo().getArgumentValues());
        tccInfo.setMethodTypeParameter(info.getTransactionInfo().getParameterTypes());

        return tccInfo;
    }

    @Override
    public void preBusinessCode(TxTransactionInfo info) throws BeforeBusinessException {
        log.info(" TCC  > transaction >  starting ");
        UnitTCCInfoMap unitTCCInfoMap = new UnitTCCInfoMap();
        unitTCCInfoMap.put(info.getUnitId(), prepareTccInfo(info));
        transactionAttachmentCache.attach(info.getGroupId(), info.getUnitId(), unitTCCInfoMap);

        // 创建事务组
        transactionControlTemplate.createGroup(
                info.getGroupId(), info.getUnitId(), info.getTransactionInfo(), info.getTransactionType());
    }

    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        DTXLocal.cur().setState(0);
    }

    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) {
        DTXLocal.cur().setState(1);
    }

    /**
     * 事务发起方 自己执行  提交 / 取消 事件
     *
     * @param info
     */
    @Override
    public void postBusinessCode(TxTransactionInfo info) {
        transactionControlTemplate.notifyGroup(
                info.getGroupId(), info.getUnitId(), info.getTransactionType(), DTXLocal.cur().getState());
    }
}
