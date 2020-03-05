package com.codingapi.txlcn.tc.info;

import com.codingapi.txlcn.tc.parser.TxAnnotation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 *
 */
@Slf4j
public class TransactionInfoManager {

    private final TransactionInfo transactionInfo;

    public TransactionInfoManager(TxAnnotation txAnnotation){
        TransactionInfo transactionInfo =  TransactionInfoThreadLocal.current();
        if(transactionInfo ==null){
            if(txAnnotation!=null){
                transactionInfo = new TransactionInfo();
                transactionInfo.setTransactionType(txAnnotation.getType());
                transactionInfo.setTransmitTransaction(false);
                TransactionInfoThreadLocal.push(transactionInfo);
            }
        }else{
            transactionInfo.setTransmitTransaction(true);
        }
        this.transactionInfo = transactionInfo;
    }

    public boolean existTransaction(){
        return transactionInfo !=null;
    }

    public TransactionInfo getTransactionInfo() {
        return transactionInfo;
    }
}
