package com.codingapi.txlcn.tc.parser;

import java.lang.reflect.Method;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
public interface AnnotationParser {

    TxAnnotation getAnnotation(Method targetMethod);

}
