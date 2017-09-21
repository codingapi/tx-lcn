package com.lorne.tx.bean;

/**
 * 切面控制对象
 * Created by lorne on 2017/6/8.
 */
public class TxTransactionCompensate {

    private final static ThreadLocal<TxTransactionCompensate> currentLocal = new ThreadLocal<TxTransactionCompensate>();


    public TxTransactionCompensate() {

    }


    public static TxTransactionCompensate current() {
        return currentLocal.get();
    }

    public static void setCurrent(TxTransactionCompensate current) {
        currentLocal.set(current);
    }


}
