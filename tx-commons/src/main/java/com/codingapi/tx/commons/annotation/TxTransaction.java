package com.codingapi.tx.commons.annotation;

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
     * 是否LCN事务发起方
     * @return true 是:是发起方 false 否:是参与方
     */
    boolean isStart() default false;

    /**
     * 事务模式
     * @return
     */
    String type() default "lcn";



}
