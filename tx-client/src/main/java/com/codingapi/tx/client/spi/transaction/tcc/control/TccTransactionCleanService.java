package com.codingapi.tx.client.spi.transaction.tcc.control;

import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.client.spi.transaction.tcc.control.UnitTCCInfoMap;
import com.codingapi.tx.commons.exception.TransactionClearException;
import com.codingapi.tx.client.bean.TCCTransactionInfo;
import com.codingapi.tx.client.support.common.cache.TransactionAttachmentCache;
import com.codingapi.tx.client.support.common.TransactionCleanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author 侯存路
 */
@Component
@Slf4j
public class TccTransactionCleanService implements TransactionCleanService {

    @Autowired
    private TransactionAttachmentCache transactionAttachmentCache;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void clear(String groupId, int state, String unitId, String unitType) throws TransactionClearException {
        Optional<UnitTCCInfoMap> optional = transactionAttachmentCache.attachment(groupId, UnitTCCInfoMap.class);
        UnitTCCInfoMap unitTCCInfoMap = optional.get();
        TCCTransactionInfo tccInfo = unitTCCInfoMap.get(unitId);

        Object object = applicationContext.getBean(tccInfo.getExecuteClass());
        Method exeMethod = null;
        try {
            DTXLocal.getOrNew().setGroupId(groupId);
            DTXLocal.getOrNew().setUnitId(unitId);
            exeMethod = tccInfo.getExecuteClass().getMethod(
                    state == 1 ? tccInfo.getConfirmMethod() : tccInfo.getCancelMethod(),
                    tccInfo.getMethodTypeParameter());
            exeMethod.invoke(object, tccInfo.getMethodParameter());
            // 清理与事务组生命周期一样的资源
            transactionAttachmentCache.removeAttachments(groupId, unitId);
        } catch (Exception e) {
            log.error(" rpc_tcc_" + exeMethod + e.getMessage());
            throw new TransactionClearException(e.getMessage());
        } finally {
            DTXLocal.makeNeverAppeared();
        }
    }
}
