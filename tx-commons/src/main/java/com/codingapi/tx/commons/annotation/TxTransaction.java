package com.codingapi.tx.commons.annotation;

import com.codingapi.tx.commons.util.Transactions;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * Created by lorne on 2017/6/26.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional
public @interface TxTransaction {

    
    /**
     * 事务模式 transaction type
     *
     * @see com.codingapi.tx.commons.util.Transactions
     * @return lcn,tcc,txc
     */
    String type() default Transactions.LCN;
    
    
}
