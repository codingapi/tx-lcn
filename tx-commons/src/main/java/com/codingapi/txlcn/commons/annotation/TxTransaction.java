package com.codingapi.txlcn.commons.annotation;

import com.codingapi.txlcn.commons.util.Transactions;

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
     * 分布式事务传播行为
     *
     * @return 传播行为
     * @see DTXPropagation
     */
    DTXPropagation dtxp() default DTXPropagation.REQUIRED;
}
