package com.codingapi.txlcn.tc.aspect;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tc.control.TransactionContext;
import com.codingapi.txlcn.tc.resolver.AnnotationContext;
import com.codingapi.txlcn.tc.resolver.LcnAnnotationStrategy;
import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

/**
 * @author lorne
 * @date 2020/7/2
 * @description
 */
 class TransactionAspectContextTest {

    private TransactionAspectContext transactionAspectContext;

    private TransactionContext transactionContext;

    private AnnotationContext annotationContext;

    private MethodInvocation methodInvocation;

    class TestMethod{

        @LcnTransaction
        int test(){
            return 1;
        }
    }

    @SneakyThrows
    @BeforeEach
    void before() {
        LcnAnnotationStrategy lcnAnnotationStrategy = new LcnAnnotationStrategy();
        annotationContext = new AnnotationContext(Collections.singletonList(lcnAnnotationStrategy));
        transactionContext = Mockito.mock(TransactionContext.class);
        methodInvocation = Mockito.mock(MethodInvocation.class);
        Mockito.when(methodInvocation.getMethod()).thenReturn(TestMethod.class.getDeclaredMethod("test"));
        transactionAspectContext = new TransactionAspectContext(transactionContext,annotationContext);
    }

    @SneakyThrows
    @Test
     void runWithTransaction() {
        transactionAspectContext.runWithTransaction(methodInvocation);
    }
}
