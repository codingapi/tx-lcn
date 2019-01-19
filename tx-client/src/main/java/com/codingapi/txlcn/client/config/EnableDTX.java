package com.codingapi.txlcn.client.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Description: 允许分布式事务的注解
 * Date: 1/19/19
 *
 * @author ujued
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({EnableDTXImportSelector.class})
public @interface EnableDTX {

    /**
     * 是否允许
     *
     * @return 决策
     */
    boolean enabled() default true;
}
