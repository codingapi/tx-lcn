package com.codingapi.txlcn.tc.resolver;

import com.codingapi.maven.uml.annotation.Model;

import java.lang.reflect.Method;

/**
 * 事务注解解析器
 * @author lorne 2020-03-05
 */
@Model(flag = "I",value = "事务注解解析器",color = "#FF88EE")
public interface AnnotationStrategy {

    TxAnnotation getAnnotation(Method targetMethod);

}
