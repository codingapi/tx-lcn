package com.codingapi.txlcn.tc.parser;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
public class AnnotationParserHelper {

    private List<AnnotationParser> parsers;

    public AnnotationParserHelper(List<AnnotationParser> parsers) {
        this.parsers = parsers;
    }

    public TxAnnotation getAnnotation(Method targetMethod) {
        if(parsers==null){
            return null;
        }
        for(AnnotationParser annotationParser:parsers){
            TxAnnotation annotation = annotationParser.getAnnotation(targetMethod);
            if(annotation!=null){
                return annotation;
            }
        }
        return null;
    }
}
