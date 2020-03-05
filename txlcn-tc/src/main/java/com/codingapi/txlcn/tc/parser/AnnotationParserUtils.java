package com.codingapi.txlcn.tc.parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
public class AnnotationParserUtils {

    private final static List<AnnotationParser> parsers = new ArrayList<>();

    static {
        init();
    }

    public static void init(){
        parsers.add(new LcnAnnotationParser());
    }

    public static void addAnnotationParser(AnnotationParser annotationParser){
        parsers.add(annotationParser);
    }

    public static TxAnnotation getAnnotation(Method targetMethod) {
        for(AnnotationParser annotationParser:parsers){
            TxAnnotation annotation = annotationParser.getAnnotation(targetMethod);
            if(annotation!=null){
                return annotation;
            }
        }
        return null;
    }
}
