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
}
