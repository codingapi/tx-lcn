package com.codingapi.txlcn.client.config;

import com.codingapi.txlcn.client.CoreConfiguration;
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
@Import({CoreConfiguration.class})
public @interface EnableDistributedTransaction {
}
