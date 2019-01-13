package com.codingapi.tx.client.aspect.interceptor;

import com.codingapi.tx.client.bean.DTXInfo;
import com.codingapi.tx.client.support.common.DTXInfoPool;
import com.codingapi.tx.commons.annotation.*;
import com.codingapi.tx.commons.util.Transactions;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Objects;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/13
 *
 * @author codingapi
 */
class InterceptorInvocationUtils {

    static DTXInfo load(MethodInvocation invocation){
        TxTransaction txTransaction = invocation.getMethod().getAnnotation(TxTransaction.class);
        String transactionType = Transactions.LCN;
        DTXPropagation dtxPropagation = DTXPropagation.REQUIRED;

        if (Objects.nonNull(txTransaction)) {
            transactionType = txTransaction.type();
            dtxPropagation = txTransaction.dtxp();
        } else {
            LcnTransaction lcnTransaction = invocation.getMethod().getAnnotation(LcnTransaction.class);
            if (Objects.nonNull(lcnTransaction)) {
                transactionType = Transactions.LCN;
                dtxPropagation = lcnTransaction.dtxp();
            } else {
                TxcTransaction txcTransaction = invocation.getMethod().getAnnotation(TxcTransaction.class);
                if (Objects.nonNull(txcTransaction)) {
                    transactionType = Transactions.TXC;
                    dtxPropagation = txcTransaction.dtxp();
                } else {
                    TccTransaction tccTransaction = invocation.getMethod().getAnnotation(TccTransaction.class);
                    if (Objects.nonNull(tccTransaction)) {
                        transactionType = Transactions.TCC;
                        dtxPropagation = tccTransaction.dtxp();
                    }
                }
            }
        }
        DTXInfo dtxInfo = DTXInfoPool.get(invocation);
        dtxInfo.setTransactionType(transactionType);
        dtxInfo.setTransactionPropagation(dtxPropagation);
        return dtxInfo;
    }
}
