//package com.lorne.tx.utils;
//
////import com.lorne.tx.compensate.model.TransactionInvocation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.ApplicationContext;
//
//
///**
// * Created by yuliang on 2017/7/11.
// */
//public class MethodUtils {
//
//    private static final Logger logger = LoggerFactory.getLogger(MethodUtils.class);
//
//    public static boolean invoke(ApplicationContext spring, TransactionInvocation invocation) {
//        try {
//            Object bean = spring.getBean(invocation.getTargetClazz());
//            Object res = org.apache.commons.lang.reflect.MethodUtils.invokeMethod(bean, invocation.getMethod(), invocation.getArgumentValues(), invocation.getParameterTypes());
//            logger.info("事务补偿执行---> className:" + invocation.getTargetClazz() + ",methodName::" + invocation.getMethod() + ",args:" + invocation.getArgumentValues() + ",res:" + res);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//}
