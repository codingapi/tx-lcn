package com.codingapi.tx.annotation;

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
     * 回滚异常
     * @return
     */
    Class<? extends Throwable>[] rollbackFor() default {};


    /**
     * 不回滚异常
     * @return
     */
    Class<? extends Throwable>[] noRollbackFor() default {};

}
