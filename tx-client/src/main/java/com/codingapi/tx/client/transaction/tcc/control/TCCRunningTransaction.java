package com.codingapi.tx.client.transaction.tcc.control;

import com.codingapi.tx.commons.annotation.TCCTransaction;
import com.codingapi.tx.client.bean.TCCInfo;
import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.bean.TxTransactionLocal;
import com.codingapi.tx.client.framework.control.LCNTransactionControl;
import com.codingapi.tx.client.transaction.common.template.TransactionCleanTemplate;
import com.codingapi.tx.client.transaction.common.template.TransactionControlTemplate;
import com.codingapi.tx.client.transaction.common.cache.TransactionAttachmentCache;
import com.codingapi.tx.commons.exception.TransactionClearException;
import com.codingapi.tx.commons.exception.TxClientException;
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
@Service(value = "control_tcc_running")
@Slf4j
public class TCCRunningTransaction implements LCNTransactionControl {

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
    public void preBusinessCode(TxTransactionInfo info) {
        log.info(" TCC  > transaction >  running ");
        String groupId = info.getGroupId();
        Method method = info.getPointMethod();

        TCCTransaction tccTransaction = method.getAnnotation(TCCTransaction.class);
        if (tccTransaction == null) {
            throw new IllegalStateException(" TCC模式下需添加 @TCCTransaction 注解在  " + method.getName() + " 上 ！");
        }
        if (StringUtils.isEmpty(tccTransaction.cancelMethod())) {
            throw new NullPointerException("  @TCCTransaction 需指明 回滚执行方法名称！ ");
        }
        if (StringUtils.isEmpty(tccTransaction.confirmMethod())) {
            throw new NullPointerException("  @TCCTransaction 需指明 确认执行方法名称！");
        }


        TCCInfo tccInfo = new TCCInfo();
        tccInfo.setExecuteClass(tccTransaction.executeClass());
        tccInfo.setCancelMethod(tccTransaction.cancelMethod());
        tccInfo.setConfirmMethod(tccTransaction.confirmMethod());
        tccInfo.setMethodParameter(info.getTransactionInfo().getArgumentValues());
        tccInfo.setMethodTypeParameter(info.getTransactionInfo().getParameterTypes());
        UnitTCCInfoMap map = transactionAttachmentCache.attachment(groupId, info.getUnitId(), UnitTCCInfoMap.class, UnitTCCInfoMap::new);
        map.put(info.getUnitId(), tccInfo);
    }

    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        try {
            transactionCleanTemplate.clean(
                    TxTransactionLocal.current().getGroupId(),
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
