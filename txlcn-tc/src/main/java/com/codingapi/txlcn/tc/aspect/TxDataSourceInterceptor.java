package com.codingapi.txlcn.tc.aspect;

import com.codingapi.txlcn.p6spy.CompoundJdbcEventListener;
import com.codingapi.txlcn.p6spy.common.ConnectionInformation;
import com.codingapi.txlcn.p6spy.wrapper.ConnectionWrapper;
import com.codingapi.txlcn.tc.info.TransactionInfo;
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

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionInfo transactionInfo = TransactionInfo.current();
        Connection connection = (Connection) invocation.proceed();
        if(transactionInfo!=null&&transactionInfo.hasSqlProxy()) {
            return new ProxyConnection(ConnectionWrapper.wrap(connection,
                    compoundJdbcEventListener,
                    ConnectionInformation.fromConnection(connection)));
        }else{
            return connection;
        }
    }
}
