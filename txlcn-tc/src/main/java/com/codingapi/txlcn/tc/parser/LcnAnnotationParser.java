package com.codingapi.txlcn.tc.parser;

import com.codingapi.txlcn.tc.TransactionContent;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;

import java.lang.reflect.Method;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
public class LcnAnnotationParser implements AnnotationParser {

    @Override
    public TxAnnotation getAnnotation(Method targetMethod) {
        LcnTransaction lcnTransaction =  targetMethod.getAnnotation(LcnTransaction.class);
        if(lcnTransaction!=null){
            return new TxAnnotation(lcnTransaction, TransactionContent.LCN);
        }
        return null;
    }
}
