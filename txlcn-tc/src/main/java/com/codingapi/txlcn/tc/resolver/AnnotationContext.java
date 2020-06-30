package com.codingapi.txlcn.tc.resolver;

import com.codingapi.maven.uml.annotation.GraphRelation;
import com.codingapi.maven.uml.annotation.Model;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Model(flag = "C",value = "注解环境信息",color = "#FF88EE")
public class AnnotationContext {

    @GraphRelation(value = "*-->",type = AnnotationStrategy.class)
    private List<AnnotationStrategy> strategies;

    public AnnotationContext(List<AnnotationStrategy> strategies) {
        this.strategies = strategies;
    }

    @GraphRelation(value = "..>",type = TxAnnotation.class)
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
