package com.codingapi.txlcn.tc.resolver;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
public class AnnotationContext {

    private List<AnnotationStrategy> strategies;

    public AnnotationContext(List<AnnotationStrategy> strategies) {
        this.strategies = strategies;
    }

    public TxAnnotation getAnnotation(Method targetMethod) {
        if(strategies==null){
            return null;
        }
        for(AnnotationStrategy annotationStrategy :strategies){
            TxAnnotation annotation = annotationStrategy.getAnnotation(targetMethod);
            if(annotation!=null){
                return annotation;
            }
        }
        return null;
    }
}
