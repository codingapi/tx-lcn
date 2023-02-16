package com.codingapi.tx.datasource.relational;

import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.datasource.ICallClose;
import com.codingapi.tx.datasource.ILCNResource;
import com.codingapi.tx.datasource.service.DataSourceService;
import com.codingapi.tx.framework.task.TaskGroup;
import com.codingapi.tx.framework.task.TaskGroupManager;
import com.codingapi.tx.framework.task.TaskState;
import com.codingapi.tx.framework.task.TxTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;


/**
 * create by lorne on 2017/7/29
 */
public class LCNDBConnection extends AbstractTransactionThread implements LCNConnection {


    private Logger logger = LoggerFactory.getLogger(LCNDBConnection.class);

    private ThreadLocal<Boolean> isClose = new ThreadLocal<>();

    private volatile int state = 1;

    private Connection connection;

    private DataSourceService dataSourceService;

    private ICallClose<ILCNResource> runnable;

    private int maxOutTime;

    private String groupId;

    private TxTask waitTask;

    private boolean readOnly = false;


    public LCNDBConnection(Connection connection, DataSourceService dataSourceService, ICallClose<ILCNResource> runnable) {
        logger.debug("init lcn connection ! ");
        this.connection = connection;
        this.runnable = runnable;
        this.dataSourceService = dataSourceService;
        TxTransactionLocal transactionLocal = TxTransactionLocal.current();
        groupId = transactionLocal.getGroupId();
        maxOutTime = transactionLocal.getMaxTimeOut();

        //创建任务，key为kId
        TaskGroup taskGroup = TaskGroupManager.getInstance().createTask(transactionLocal.getKid(),transactionLocal.getType());
        waitTask = taskGroup.getCurrent();
    }



    @Override
    public void commit() throws SQLException {

        logger.debug("commit label");

        //标记成功执行。
        state = 1;

        close();

        //设置该db已关闭
        isClose.set(true);

    }

    @Override
    public void rollback() throws SQLException {

        logger.debug("rollback label");

        state = 0;

        close();

        isClose.set(true);
    }

    @Override
    protected void closeConnection() throws SQLException {
        //删除任务
        runnable.close(this);
        //关闭连接
        connection.close();
        logger.debug("lcnConnection closed groupId:"+ groupId);
    }

    @Override
    public void close() throws SQLException {

        //这里会调用二次close，一次是commit时调用。另外一次是spring关闭时调用。
        //若已经close则返回。
        if(isClose.get()!=null&& isClose.get()){
            return;
        }

        //db连接关闭则返回
        if(connection==null||connection.isClosed()){
            return;
        }

        //针对连接只读的处理。
        if(readOnly){
            closeConnection();
            logger.debug("now transaction is readOnly , groupId:" + groupId);
            return;
        }

        logger.debug("now transaction state is " + state + ", (1:commit,0:rollback) groupId:" + groupId);

        //执行失败，回滚的处理。
        if (state==0) {
            //回滚
            rollbackConnection();
            closeConnection();

            logger.debug("rollback transaction ,groupId:" + groupId);
        }
        //执行成功
        if (state==1) {
            TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
            //同一个模块被多次请求则直接返回。
            boolean hasGroup = (txTransactionLocal!=null)?txTransactionLocal.isHasIsGroup():false;
            if (hasGroup) {
                //加入队列的连接，仅操作连接对象，不处理事务
                logger.debug("connection hasGroup -> "+hasGroup);
                return;
            }
            startRunnable();
        }

    }

    @Override
    protected void rollbackConnection() throws SQLException {
        connection.rollback();
    }

    @Override
    public void transaction() throws SQLException {
        if (waitTask == null) {
            rollbackConnection();
            System.out.println(" waitTask is null");
            return;
        }


        //start 结束就是全部事务的结束表示,考虑start挂掉的情况
        Timer timer = new Timer();
        System.out.println(" maxOutTime : "+maxOutTime);
        //1：等待5秒，倘若没收到tx-m返回的事务执行结果，主动询问
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                logger.info("auto execute ,groupId:" + getGroupId());
                dataSourceService.schedule(getGroupId(), waitTask);
            }
        }, maxOutTime);

        logger.info("transaction is wait for TxManager notify, groupId {}", getGroupId());

        //2：阻塞等待，等待tx-m返回的结果
        waitTask.awaitTask();
        //说明获取到结果，定时任务取消。
        timer.cancel();

        //3：获取返回的结果
        int rs = waitTask.getState();

        try {
            //4：说明发起方调用方执行成功，则提交事务。
            if (rs == 1) {
                connection.commit();
            } else {
                //5：说明其他方执行失败，则回滚事务
                rollbackConnection();
            }

            logger.info("lcn transaction over, res -> groupId:"+getGroupId()+" and  state is "+(rs==1?"commit":"rollback"));

        }catch (SQLException e){
            logger.info("lcn transaction over,but connection is closed, res -> groupId:"+getGroupId());
            //记录连接错误。
            waitTask.setState(TaskState.connectionError.getCode());
        }

        //6：删除任务
        waitTask.remove();

    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public TxTask getWaitTask() {
        return waitTask;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if(connection!=null) {
            connection.setAutoCommit(false);
        }
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



    /*****default*******/

    @Override
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return connection.prepareCall(sql);
    }

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
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
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
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return connection.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return connection.prepareStatement(sql, columnNames);
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
}
