package com.codingapi.txlcn.tc.resolver;

import com.codingapi.txlcn.tc.constant.TransactionConstant;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;

import java.lang.reflect.Method;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
public class LcnAnnotationStrategy implements AnnotationStrategy {

    @Override
    public TxAnnotation getAnnotation(Method targetMethod) {
        LcnTransaction lcnTransaction =  targetMethod.getAnnotation(LcnTransaction.class);
        if(lcnTransaction!=null){
            return new TxAnnotation(lcnTransaction, TransactionConstant.LCN);
        }
        return null;
    }
}
