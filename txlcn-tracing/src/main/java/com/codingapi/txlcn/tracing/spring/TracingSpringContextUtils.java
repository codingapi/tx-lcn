package com.codingapi.txlcn.tracing.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * spring工厂调用辅助类
 *
 * @author dinghuang123@gmail.com
 * @since 2019/2/26
 */
public class TracingSpringContextUtils implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(TracingSpringContextUtils.class);

    private static DefaultListableBeanFactory springFactory;

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        LOGGER.info("设置Spring上下文");
        context = applicationContext;
        if (applicationContext instanceof AbstractRefreshableApplicationContext) {
            AbstractRefreshableApplicationContext springContext =
                    (AbstractRefreshableApplicationContext) applicationContext;
            springFactory = (DefaultListableBeanFactory) springContext.getBeanFactory();
        } else if (applicationContext instanceof GenericApplicationContext) {
            GenericApplicationContext springContext = (GenericApplicationContext) applicationContext;
            springFactory = springContext.getDefaultListableBeanFactory();
        }
    }

    public static DefaultListableBeanFactory getSpringFactory() {
        return springFactory;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
