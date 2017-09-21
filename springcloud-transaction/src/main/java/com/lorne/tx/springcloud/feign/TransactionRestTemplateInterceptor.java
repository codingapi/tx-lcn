package com.lorne.tx.springcloud.feign;

import com.lorne.tx.bean.TxTransactionLocal;
import com.lorne.tx.compensate.service.impl.CompensateServiceImpl;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Created by lorne on 2017/6/26.
 */
public class TransactionRestTemplateInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        String groupId = txTransactionLocal == null ? null : txTransactionLocal.getGroupId();
        int maxTimeOut = txTransactionLocal == null ? 0 : txTransactionLocal.getMaxTimeOut();
        if (txTransactionLocal != null) {
            if (txTransactionLocal.isHasCompensate()) {
                requestTemplate.header("tx-group", CompensateServiceImpl.COMPENSATE_KEY);
            } else {
                requestTemplate.header("tx-group", groupId);
                requestTemplate.header("tx-maxTimeOut", String.valueOf(maxTimeOut));
            }
        }
    }

}
