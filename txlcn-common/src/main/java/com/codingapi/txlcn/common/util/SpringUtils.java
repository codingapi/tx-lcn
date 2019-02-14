package com.codingapi.txlcn.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Description:
 * Date: 19-2-14 上午11:37
 *
 * @author ujued
 */
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> type) {
        try {
            return Optional.ofNullable(applicationContext).orElseThrow(() -> new IllegalStateException("non in spring application context.")).getBean(type);
        } catch (Exception e) {
            return null;
        }
    }
}
