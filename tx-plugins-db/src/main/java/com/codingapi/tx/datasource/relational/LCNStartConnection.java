package com.codingapi.tx.datasource.relational;

import com.codingapi.tx.aop.bean.TxCompensateLocal;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.datasource.ICallClose;
import com.codingapi.tx.datasource.ILCNResource;
import com.codingapi.tx.framework.task.TaskGroup;
import com.codingapi.tx.framework.task.TaskGroupManager;
import com.codingapi.tx.framework.task.TaskState;
import com.codingapi.tx.framework.task.TxTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * create by lorne on 2017/12/1
 */
public class LCNStartConnection extends AbstractTransactionThread implements LCNConnection{

    private Logger logger = LoggerFactory.getLogger(LCNStartConnection.class);

    private Connection connection;

    private ICallClose<ILCNResource> subNowCount;

    private String groupId;

    private TxTask waitTask;

    private volatile int state = 1;

    private boolean isCompensate = false;

    private int startState = 0;

    private ThreadLocal<Boolean> isClose = new ThreadLocal<>();


    public LCNStartConnection(Connection connection, ICallClose<ILCNResource> subNowCount) {
        this.connection = connection;
        this.subNowCount = subNowCount;

        if(TxCompensateLocal.current()!=null){
            isCompensate = true;
            logger.info("transaction is compensate-connection.");

            TxCompensateLocal txCompensateLocal = TxCompensateLocal.current();
            groupId =  txCompensateLocal.getGroupId();

            TaskGroup taskGroup = TaskGroupManager.getInstance().createTask(groupId,txCompensateLocal.getType());
            waitTask = taskGroup.getCurrent();

            startState = txCompensateLocal.getStartState();
        }else{
            isCompensate = false;
            logger.info("transaction is start-connection.");
            TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
            groupId = txTransactionLocal.getGroupId();

            //新建任务
            TaskGroup taskGroup = TaskGroupManager.getInstance().createTask(groupId, txTransactionLocal.getType());
            //将新建的任务捞出赋给waitTask
            waitTask = taskGroup.getCurrent();
        }

        logger.info("lcn start connection init ok .");
    }

    @Override
    public TxTask getWaitTask() {
        return waitTask;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }


    @Override
    public void commit() throws SQLException {

        logger.info("commit label");
        //标记正确执行
        state=1;
        //异步线程执行commit或者rollback
        close();
        //设置已经执行过close
        isClose.set(true);
    }

    @Override
    public void rollback() throws SQLException {

        logger.info("rollback label");
        //标记执行失败。
        state=0;
        //执行rollback，归还线程资源。
        close();
        //设置已经执行过close
        isClose.set(true);
    }

    @Override
    public void close() throws SQLException {

        if(isClose.get()!=null&& isClose.get()){
            return;
        }

        if(connection==null||connection.isClosed()){
            return;
        }
        //1：回滚的处理
        if(state==0){
            //数据库回滚
            rollbackConnection();
            //关闭回调函数，关闭task，从缓存中清除db连接，归还db连接。
            closeConnection();
            return;
        }
        //2：正确执行的处理
        if(state == 1) {
            TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
            boolean isGroup = (txTransactionLocal != null) ? txTransactionLocal.isHasIsGroup() : false;
            //不是同一个db连接调用多次
            if (isGroup) {
                //加入队列的连接，仅操作连接对象，不处理事务
                logger.info("start connection hasGroup -> " + isGroup);
                return;
            }
            //3：异步线程执行，等待txm返回的最终结果，决定是否commit或者rollback。
            startRunnable();
        }
    }

    @Override
    protected void rollbackConnection() throws SQLException {
        connection.rollback();
    }

    @Override
    public void transaction()throws SQLException{
        if (waitTask == null) {
            rollbackConnection();
            System.out.println(" start waitTask is null");
            return;
        }

        logger.info(" start transaction is wait for TxManager notify, groupId : " + getGroupId());
        //1：这里会阻塞等待，直到tx-m通知所有参与者提交，获取到所有参与者执行后的最终结果后，才会被唤醒。
        waitTask.awaitTask();
        //获取最终执行结果。
        int rs = waitTask.getState();

        try {
            //说明tx-m返回成功
            if (rs == 1) {
                if(isCompensate) {
                    //2：补偿时需要根据补偿数据决定提交还是回滚.
                    rs = startState;
                    if(rs==1) {
                        connection.commit();
                    }else{
                        connection.rollback();
                    }
                }else{
                    //3：提交
                    connection.commit();
                }
            } else {
                //tx-m
                //4：回滚事务
                rollbackConnection();
            }
            logger.info(" lcn start transaction over, res -> groupId:"+getGroupId()+" and  state is "+(rs==1?"commit":"rollback"));

        }catch (SQLException e){
            //回滚事务
            rollbackConnection();
            //记录发起方提交失败
            waitTask.setState(TaskState.connectionError.getCode());

            System.out.println(" lcn start transaction over,but connection is closed,  res -> groupId:"+getGroupId());
        }
        //删除任务
        waitTask.remove();

    }

    @Override
    protected void closeConnection() throws SQLException{
        //关闭task，从缓存中清除db连接
        subNowCount.close(this);

        //spring封装的连接池归还
        connection.close();
    }



    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if(connection!=null) {
            connection.setAutoCommit(false);
        }
    }



    /**
     * default
     */

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
    public void setReadOnly(boolean readOnly) throws SQLException {
        connection.setReadOnly(readOnly);
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
