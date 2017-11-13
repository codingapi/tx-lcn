package com.codingapi.tx.framework.utils;


import com.codingapi.tx.model.TransactionInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * create by lorne on 2017/11/13
 */
public class MethodUtils {

    private static final Logger logger = LoggerFactory.getLogger(MethodUtils.class);

    public static boolean invoke(ApplicationContext spring, TransactionInvocation invocation) {
        try {
            Object bean = spring.getBean(invocation.getTargetClazz());
            Object res = org.apache.commons.lang.reflect.MethodUtils.invokeMethod(bean, invocation.getMethod(), invocation.getArgumentValues(), invocation.getParameterTypes());
            logger.info("事务补偿执行---> className:" + invocation.getTargetClazz() + ",methodName::" + invocation.getMethod() + ",args:" + invocation.getArgumentValues() + ",res:" + res);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
