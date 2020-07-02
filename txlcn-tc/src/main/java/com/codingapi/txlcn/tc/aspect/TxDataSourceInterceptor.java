package com.codingapi.txlcn.tc.aspect;

import com.codingapi.txlcn.p6spy.CompoundJdbcEventListener;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import com.codingapi.txlcn.tc.jdbc.JdbcTransactionDataSource;
import com.codingapi.txlcn.tc.jdbc.ProxyConnection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.sql.Connection;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Slf4j
@AllArgsConstructor
public class TxDataSourceInterceptor implements MethodInterceptor {

    private CompoundJdbcEventListener compoundJdbcEventListener;

    private JdbcTransactionDataSource jdbcTransactionDataSource;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionInfo transactionInfo = TransactionInfo.current();
        Connection connection = (Connection) invocation.proceed();
        if(transactionInfo!=null&&transactionInfo.hasSqlProxy()) {
            //获取一个全局的连接对象,用于执行日志保存
            if(jdbcTransactionDataSource.noConnection()) {
                jdbcTransactionDataSource.setConnection(connection);
                connection = (Connection) invocation.proceed();
            }
            return new ProxyConnection(compoundJdbcEventListener,connection);
        }else{
            return connection;
        }
    }
}
