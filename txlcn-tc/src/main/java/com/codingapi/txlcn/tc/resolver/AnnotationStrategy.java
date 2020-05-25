package com.codingapi.txlcn.tc.resolver;

import java.lang.reflect.Method;

/**
 * 事务注解解析器
 * @author lorne 2020-03-05
 */
public interface AnnotationStrategy {

    TxAnnotation getAnnotation(Method targetMethod);

}
