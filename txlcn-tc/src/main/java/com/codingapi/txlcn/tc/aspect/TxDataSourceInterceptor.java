package com.codingapi.txlcn.tc.aspect;

import com.codingapi.txlcn.p6spy.CompoundJdbcEventListener;
import com.codingapi.txlcn.p6spy.common.ConnectionInformation;
import com.codingapi.txlcn.p6spy.wrapper.ConnectionWrapper;
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
        Connection connection = (Connection) invocation.proceed();
        return ConnectionWrapper.wrap(connection,
                compoundJdbcEventListener,
                ConnectionInformation.fromConnection(connection));
    }
}
