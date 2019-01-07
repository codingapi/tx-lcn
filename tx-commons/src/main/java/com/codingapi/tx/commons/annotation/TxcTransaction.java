package com.codingapi.tx.commons.annotation;

import java.lang.annotation.*;

/**
 * Description: type [txc] of DTX
 * Date: 1/4/19
 *
 * @author ujued
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TxcTransaction {

    /**
     * 资源锁定时等待时间，默认不等待
     *
     * @return
     */
    long timeout() default 0;
}
