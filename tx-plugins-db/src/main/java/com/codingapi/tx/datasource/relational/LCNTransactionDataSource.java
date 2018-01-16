package com.codingapi.tx.datasource.relational;

import com.codingapi.tx.aop.bean.TxCompensateLocal;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.datasource.AbstractResourceProxy;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;


/**
 * 关系型数据库代理连接池对象
 * create by lorne on 2017/7/29
 */

public class LCNTransactionDataSource extends AbstractResourceProxy<Connection,LCNDBConnection> implements DataSource {


    private org.slf4j.Logger logger = LoggerFactory.getLogger(LCNTransactionDataSource.class);


    protected DataSource dataSource;


    protected DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected Connection createLcnConnection(Connection connection, TxTransactionLocal txTransactionLocal) {
        nowCount++;
        if(txTransactionLocal.isHasStart()){
            LCNStartConnection lcnStartConnection = new LCNStartConnection(connection,subNowCount);
            logger.info("get new start connection - > "+txTransactionLocal.getGroupId());
            pools.put(txTransactionLocal.getGroupId(), lcnStartConnection);
            txTransactionLocal.setHasConnection(true);
            return lcnStartConnection;
        }else {
            LCNDBConnection lcn = new LCNDBConnection(connection, dataSourceService, subNowCount);
            logger.info("get new connection ->" + txTransactionLocal.getGroupId());
            pools.put(txTransactionLocal.getGroupId(), lcn);
            txTransactionLocal.setHasConnection(true);
            return lcn;
        }
    }


//    @Override
//    protected Connection getCompensateConnection(Connection connection, TxCompensateLocal txCompensateLocal) {
//        return new LCNCompensateDBConnection(connection,txCompensateLocal);
//    }

    @Override
    protected void initDbType() {
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        if(txTransactionLocal!=null) {
            //设置db类型
            txTransactionLocal.setType("datasource");
        }
        TxCompensateLocal txCompensateLocal = TxCompensateLocal.current();
        if(txCompensateLocal!=null){
            //设置db类型
            txCompensateLocal.setType("datasource");
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        initDbType();

        Connection connection =(Connection)loadConnection();
        if(connection==null) {
             connection = initLCNConnection(getDataSource().getConnection());
            if(connection==null){
                throw new SQLException("connection was overload");
            }
            return connection;
        }else {
            return connection;
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {

        initDbType();

        Connection connection = (Connection)loadConnection();
        if(connection==null) {
            connection =  initLCNConnection(getDataSource().getConnection(username, password));
            if(connection==null){
                throw new SQLException("connection was overload");
            }
            return connection;
        }else {
            return connection;
        }
    }


    /**default**/

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return getDataSource().getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        getDataSource().setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        getDataSource().setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return getDataSource().getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return getDataSource().getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return getDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return getDataSource().isWrapperFor(iface);
    }
}
