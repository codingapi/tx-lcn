package com.codingapi.txlcn.tc.resolver;

import com.codingapi.maven.uml.annotation.GraphRelation;
import com.codingapi.maven.uml.annotation.Model;
import com.codingapi.txlcn.tc.TransactionContent;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;

import java.lang.reflect.Method;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Model(flag = "C",value = "LCN注解解析器",color = "#FF88EE")
@GraphRelation(value = "..|>",type = AnnotationStrategy.class)
public class LcnAnnotationStrategy implements AnnotationStrategy {

    @Override
    public TxAnnotation getAnnotation(Method targetMethod) {
        LcnTransaction lcnTransaction =  targetMethod.getAnnotation(LcnTransaction.class);
        if(lcnTransaction!=null){
            return new TxAnnotation(lcnTransaction, TransactionContent.LCN);
        }
        return null;
    }
}
