package com.codingapi.txlcn.tc.aspect;

import com.codingapi.txlcn.p6spy.CompoundJdbcEventListener;
import com.codingapi.txlcn.tc.config.TxConfig;
import com.codingapi.txlcn.tc.control.TransactionContext;
import com.codingapi.txlcn.tc.jdbc.JdbcTransactionDataSource;
import com.codingapi.txlcn.tc.resolver.AnnotationContext;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AspectConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public TransactionAspectContext transactionAspectManager(TransactionContext transactionContext,
                                                           AnnotationContext annotationContext) {
    return new TransactionAspectContext(transactionContext, annotationContext);
  }

  @Bean
  public Advisor txTransactionAdvisor(TxTransactionInterceptor txTransactionInterceptor, TxConfig txConfig){
    AspectJExpressionPointcut pointcut=new AspectJExpressionPointcut();
    pointcut.setExpression(txConfig.getTransactionPointcut());
    return new DefaultPointcutAdvisor(pointcut, txTransactionInterceptor);
  }


  @Bean
  public Advisor txDataSourceAdvisor(TxDataSourceInterceptor txDataSourceInterceptor, TxConfig txConfig){
    AspectJExpressionPointcut pointcut=new AspectJExpressionPointcut();
    pointcut.setExpression(txConfig.getDatasourcePointcut());
    return new DefaultPointcutAdvisor(pointcut, txDataSourceInterceptor);
  }


  @Bean
  public TxTransactionInterceptor txTransactionInterceptor(TransactionAspectContext transactionAspectContext){
    return new TxTransactionInterceptor(transactionAspectContext);
  }

  @Bean
  public TxDataSourceInterceptor txDataSourceInterceptor(CompoundJdbcEventListener compoundJdbcEventListener, JdbcTransactionDataSource jdbcTransactionDataSource){
    return new TxDataSourceInterceptor(compoundJdbcEventListener,jdbcTransactionDataSource);
  }


}
