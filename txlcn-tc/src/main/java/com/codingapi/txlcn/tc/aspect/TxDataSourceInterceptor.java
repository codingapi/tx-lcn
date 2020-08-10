package com.codingapi.txlcn.tc.aspect;

import com.codingapi.txlcn.p6spy.CompoundJdbcEventListener;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import com.codingapi.txlcn.tc.jdbc.JdbcTransactionDataSource;
import com.codingapi.txlcn.tc.jdbc.ProxyConnection;
import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.JdbcAnalyseUtils;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.sql.Connection;
import java.util.List;

/**
 * jdbc connection 代理拦截
 * @author lorne 2020/7/1
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

        //数据库表字段分析
        String catalog = connection.getCatalog();
        TableList tableList =  DataBaseContext.getInstance().get(connection);
        if(tableList==null||tableList.isEmpty()){
            DataBaseContext.getInstance().push(catalog,JdbcAnalyseUtils.analyse(connection));
        }

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
