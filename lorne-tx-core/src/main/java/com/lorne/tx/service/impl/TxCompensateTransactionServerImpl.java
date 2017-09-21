package com.lorne.tx.service.impl;

import com.lorne.tx.bean.TxTransactionInfo;
import com.lorne.tx.bean.TxTransactionLocal;
import com.lorne.tx.compensate.service.CompensateService;
import com.lorne.tx.service.TransactionServer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

/**
 * Created by yuliang on 2017/7/11.
 */
@Service(value = "txCompensateTransactionServer")
public class TxCompensateTransactionServerImpl implements TransactionServer {

    @Override
    public Object execute(final ProceedingJoinPoint point, TxTransactionInfo info) throws Throwable {

        TxTransactionLocal txTransactionLocal = new TxTransactionLocal();
        txTransactionLocal.setHasCompensate(true);
        txTransactionLocal.setGroupId(CompensateService.COMPENSATE_KEY);
        TxTransactionLocal.setCurrent(txTransactionLocal);

        try {
            return point.proceed();
        } catch (Throwable e) {
            throw e;
        } finally {
            TxTransactionLocal.setCurrent(null);
        }

    }
}
