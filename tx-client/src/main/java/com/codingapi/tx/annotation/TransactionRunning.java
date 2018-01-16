package com.codingapi.tx.annotation;

import java.lang.annotation.*;

/**
 * create by lorne on 2018/1/16
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TransactionRunning {
}
