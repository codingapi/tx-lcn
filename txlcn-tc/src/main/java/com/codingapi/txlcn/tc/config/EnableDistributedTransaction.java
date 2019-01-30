package com.codingapi.txlcn.tc.config;

import com.codingapi.txlcn.tc.TCAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Description: use TxLcn
 * Date: 1/19/19
 *
 * @author ujued
 * @since 5.0.0.RC2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(TCAutoConfiguration.class)
public @interface EnableDistributedTransaction {

}