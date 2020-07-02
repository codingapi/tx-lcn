package com.codingapi.txlcn.tc.control;

import com.codingapi.txlcn.tc.info.TransactionInfo;

/**
 * @author lorne
 * @date 2020/6/30
 * @description
 */
public class TransactionStateStrategy {

    /**
     * 事务状态判定
     *  事务状态在创建时是没有TransactionInfo对象的,而在JOIN状态时则是会先创建TransactionInfo
     *  对象，因此有值时返回JOIN
     * @return state
     */
    public static TransactionState getTransactionState(){
        TransactionInfo transactionInfo =  TransactionInfo.current();
        if(transactionInfo==null){
            return TransactionState.CREATE;
        }else {
            return TransactionState.JOIN;
        }
    }
}
