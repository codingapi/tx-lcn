package com.codingapi.tx.commons.annotation;

import java.lang.annotation.*;

/**
 * Description: type [lcn] of DTX
 * Date: 1/4/19
 *
 * @author ujued
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LcnTransaction {

    /**
     * 分布式事务传播行为
     *
     * @return 传播行为
     * @see DTXPropagation
     */
    DTXPropagation dtxp() default DTXPropagation.REQUIRED;
}
