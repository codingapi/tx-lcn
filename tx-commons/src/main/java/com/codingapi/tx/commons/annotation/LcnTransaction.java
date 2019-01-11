package com.codingapi.tx.commons.annotation;

import com.codingapi.tx.commons.util.DTXFunctions;

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
     * 事务单元职责
     *
     * @return 功能代号
     * @see DTXFunctions
     */
    int func() default DTXFunctions.CREATE_OR_JOIN;
}
