package com.codingapi.tx.springcloud.feign;

import com.codingapi.tx.bean.TxTransactionLocal;
//import CompensateServiceImpl;
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
            requestTemplate.header("tx-group", groupId);
            requestTemplate.header("tx-maxTimeOut", String.valueOf(maxTimeOut));
        }
    }

}
