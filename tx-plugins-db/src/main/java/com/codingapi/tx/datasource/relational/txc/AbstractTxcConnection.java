package com.codingapi.tx.datasource.relational.txc;

import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.datasource.relational.AbstractTransactionThread;
import com.codingapi.tx.datasource.relational.LCNConnection;
import com.codingapi.tx.datasource.relational.txc.parser.TxcRuntimeContext;
import com.codingapi.tx.datasource.relational.txc.rollback.TxcRollbackService;
import com.codingapi.tx.datasource.service.DataSourceService;
import com.codingapi.tx.framework.task.TaskGroup;
import com.codingapi.tx.framework.task.TaskGroupManager;
import com.codingapi.tx.framework.task.TxTask;
import com.codingapi.tx.framework.thread.HookRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;


/**
 * created at 20171116
 * @author caisirius
 */
public abstract class AbstractTxcConnection extends AbstractTransactionThread
        implements LCNConnection,TxcRuntimeContextService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTxcConnection.class);

    private boolean readOnly = false;

    volatile int state = 1;

    private Connection connection;

    DataSourceService dataSourceService;

    TxTask waitTask;

    int maxOutTime;

    String groupId;

    TxcRuntimeContext txcRuntimeContext;

    TxcRollbackService txcRollbackService;

    @Override
    public TxcRuntimeContext getTxcRuntimeContext() {
        if (txcRuntimeContext == null) {
            this.txcRuntimeContext = new TxcRuntimeContext();
            txcRuntimeContext.setGroupId(groupId);
            txcRuntimeContext.setBranchId(waitTask.getKey());

        }
        return txcRuntimeContext;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public TxTask getWaitTask() {
        return waitTask;
    }



    public AbstractTxcConnection(Connection connection, TxTransactionLocal transactionLocal,
                                 DataSourceService dataSourceService,
                                 TxcRollbackService txcRollbackService) {
        readOnly = transactionLocal.isReadOnly();
        this.connection = connection;
        this.dataSourceService = dataSourceService;
        this.txcRollbackService = txcRollbackService;

        groupId = transactionLocal.getGroupId();
        maxOutTime = transactionLocal.getMaxTimeOut();

        TaskGroup taskGroup;
        if (transactionLocal.getKid() == null) {
            logger.info("this is txc start-connection");
            taskGroup = TaskGroupManager.getInstance().createTask(groupId, transactionLocal.getType());
        } else {
            taskGroup = TaskGroupManager.getInstance().createTask(transactionLocal.getKid(), transactionLocal.getType());
        }
        waitTask = taskGroup.getCurrent();
    }

    // commit() 里要做工作
    @Override
    public void commit() throws SQLException {
        logger.info("commit");

        connection.commit();

        state = 1;
    }

    @Override
    public void rollback() throws SQLException {
        connection.rollback();
        state = 0;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
        // 只有提交才需要 开启线程等待
        if (readOnly || state == 0) {
            closeConnection();
            return;
        }
        startRunnable();
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(false);
    }


    // TODO Statement CallableStatement 也应该自定义！！
    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return connection.prepareCall(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /*****default*******/
    @Override
    public String nativeSQL(String sql) throws SQLException {
        return connection.nativeSQL(sql);
    }


    @Override
    public boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }


    @Override
    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        if(readOnly) {
            this.readOnly = readOnly;
            logger.debug("setReadOnly - >" + readOnly);
            connection.setReadOnly(readOnly);
            TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
            txTransactionLocal.setReadOnly(readOnly);
        }
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return connection.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        connection.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return connection.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        connection.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return connection.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return connection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        connection.clearWarnings();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return connection.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        connection.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        connection.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return connection.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return connection.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return connection.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        connection.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        connection.releaseSavepoint(savepoint);
    }

    @Override
    public Clob createClob() throws SQLException {
        return connection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return connection.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return connection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return connection.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return connection.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        connection.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        connection.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return connection.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return connection.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return connection.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return connection.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        connection.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return connection.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        connection.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        connection.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return connection.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return connection.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return connection.isWrapperFor(iface);
    }

    /***** wrap *******/

    @Override
    public Statement createStatement() throws SQLException {
        Statement statement = connection.createStatement();
        return new TxcStatement(statement , this);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        Statement statement = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        return new TxcStatement(statement , this);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        Statement statement = connection.createStatement(resultSetType, resultSetConcurrency);
        return new TxcStatement(statement , this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        // 这里返回自定义的 PreparedStatement
        PreparedStatement localPreparedStatement = connection.prepareStatement(sql);
        return new TxcPreparedStatement(localPreparedStatement, this, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws
            SQLException {
        PreparedStatement localPreparedStatement = connection.prepareStatement(sql, resultSetType, resultSetConcurrency);

        return new TxcPreparedStatement(localPreparedStatement, this, sql);

    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        PreparedStatement localPreparedStatement = connection.prepareStatement(sql, autoGeneratedKeys);
        return new TxcPreparedStatement(localPreparedStatement, this, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        PreparedStatement localPreparedStatement = connection.prepareStatement(sql, columnIndexes);
        return new TxcPreparedStatement(localPreparedStatement, this, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        PreparedStatement localPreparedStatement = connection.prepareStatement(sql, columnNames);
        return new TxcPreparedStatement(localPreparedStatement, this, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        PreparedStatement localPreparedStatement = connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        return new TxcPreparedStatement(localPreparedStatement, this, sql);
    }

}
