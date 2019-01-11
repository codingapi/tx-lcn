package com.codingapi.tx.commons.annotation;

import com.codingapi.tx.commons.util.DTXFunctions;
import com.codingapi.tx.commons.util.Transactions;

import java.lang.annotation.*;

/**
 * Created by lorne on 2017/6/26.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TxTransaction {


    /**
     * 事务模式 transaction type
     *
     * @return lcn, tcc, txc
     * @see Transactions
     */
    String type() default Transactions.LCN;

    /**
     * 事务单元职责
     *
     * @return 功能代号
     * @see DTXFunctions
     */
    int func() default DTXFunctions.CREATE_OR_JOIN;
}
