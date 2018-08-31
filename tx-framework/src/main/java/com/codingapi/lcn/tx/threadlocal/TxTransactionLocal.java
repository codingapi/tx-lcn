package com.codingapi.lcn.tx.threadlocal;

import com.codingapi.lcn.tx.annotation.TxTransaction;
import com.codingapi.lcn.tx.bean.TransactionInvocation;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 * @date 2018/8/31
 * @description
 */
@Slf4j
@NoArgsConstructor
@Data
public class TxTransactionLocal {

    private final static ThreadLocal<TxTransactionLocal> currentLocal = new InheritableThreadLocal<TxTransactionLocal>();

    public static TxTransactionLocal current() {
        return currentLocal.get();
    }

    public static void setCurrent(TxTransactionLocal current) {
        currentLocal.set(current);
    }

    /**
     * 切面信息
     */
    private TransactionInvocation invocation;

    /**
     * 注解信息
     */
    private TxTransaction transaction;

}
