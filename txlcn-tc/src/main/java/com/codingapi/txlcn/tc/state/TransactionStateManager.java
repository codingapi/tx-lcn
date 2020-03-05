package com.codingapi.txlcn.tc.state;

import com.codingapi.txlcn.tc.parser.AnnotationParserUtils;
import com.codingapi.txlcn.tc.parser.TxAnnotation;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 *
 */
@Slf4j
public class TransactionStateManager {

    private final TransactionState transactionState;

    public TransactionStateManager(Method targetMethod){
        TransactionState transactionState =  TransactionStateThreadLocal.current();
        if(transactionState==null){
            TxAnnotation txAnnotation = AnnotationParserUtils.getAnnotation(targetMethod);
            if(txAnnotation!=null){
                transactionState = new TransactionState();
                transactionState.setTransactionType(txAnnotation.getType());
                transactionState.setTransmitTransaction(false);
                TransactionStateThreadLocal.push(transactionState);
            }
        }else{
            transactionState.setTransmitTransaction(true);
        }
        this.transactionState = transactionState;
    }

    public boolean existTransactionState(){
        return transactionState!=null;
    }

    public TransactionState getTransactionState() {
        return transactionState;
    }
}
