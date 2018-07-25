package com.codingapi.tx.datasource.relational.txc;


import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.datasource.relational.txc.parser.ExecutePaser;
import com.codingapi.tx.datasource.relational.txc.parser.SQLType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * [类描述]
 *
 * @author caican
 * @date 17/11/28
 * @title [confluence页面的title]
 */
public class TxcStatement implements ITxcStatement {

    protected Statement statement;
    protected AbstractTxcConnection abstractTxcConnection;

    protected String sql;

    public TxcStatement(Statement localStatement, AbstractTxcConnection abstractTxcConnection) {
        this.statement = localStatement;
        this.abstractTxcConnection = abstractTxcConnection;
    }

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public Statement getStatement() {
        return statement;
    }

    private void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * @return txc 是否开启
     */
    protected boolean isInTxcTransaction(){
        return TxTransactionLocal.isInTxcTransaction();
    }

    @Override
    public boolean execute(String paramString) throws SQLException {
        setSql(paramString);
        if (isInTxcTransaction()) {
            SQLType sqlType = ExecutePaser.parse(this);
            boolean execute = this.statement.execute(paramString);
            ExecutePaser.after(this, sqlType);
            return execute;
        }

        return this.statement.execute(paramString);
    }

    @Override
    public boolean execute(String paramString, int paramInt) throws SQLException {
        setSql(paramString);
        if (isInTxcTransaction()) {
            SQLType sqlType = ExecutePaser.parse(this);
            boolean execute = this.statement.execute(paramString, paramInt);
            ExecutePaser.after(this, sqlType);
            return  execute;
        }

        return this.statement.execute(paramString, paramInt);
    }
    
    @Override
    public boolean execute(String paramString, int[] paramArrayOfInt) throws SQLException {
        setSql(paramString);
        if (isInTxcTransaction()) {
            SQLType sqlType = ExecutePaser.parse(this);
            boolean execute = this.statement.execute(paramString, paramArrayOfInt);
            ExecutePaser.after(this, sqlType);
            return  execute;
        }

        return this.statement.execute(paramString, paramArrayOfInt);
    }

    @Override
    public boolean execute(String paramString, String[] paramArrayOfString) throws SQLException {
        setSql(paramString);
        if (isInTxcTransaction()) {
            SQLType sqlType = ExecutePaser.parse(this);
            boolean execute = this.statement.execute(paramString, paramArrayOfString);
            ExecutePaser.after(this, sqlType);
            return  execute;
        }

        return this.statement.execute(paramString, paramArrayOfString);
    }

    @Override
    public int[] executeBatch() throws SQLException {
        if (! isInTxcTransaction()) {
            return this.statement.executeBatch();
        }
        throw new RuntimeException("Unsupported");
    }

    @Override
    public ResultSet executeQuery(String paramString) throws SQLException {
        setSql(paramString);
        return this.statement.executeQuery(paramString);
    }

    @Override
    public int executeUpdate(String paramString) throws SQLException {
        setSql(paramString);
        if (isInTxcTransaction()) {
            SQLType sqlType = ExecutePaser.parse(this);
            int execute = this.statement.executeUpdate(paramString);
            ExecutePaser.after(this, sqlType);
            return  execute;
        }

        return this.statement.executeUpdate(paramString);
    }

    @Override
    public int executeUpdate(String paramString, int paramInt) throws SQLException {
        setSql(paramString);
        if (isInTxcTransaction()) {
            ExecutePaser.parse(this);

        }
        return this.statement.executeUpdate(paramString, paramInt);
    }

    @Override
    public int executeUpdate(String paramString, int[] paramArrayOfInt) throws SQLException {
        setSql(paramString);
        if (isInTxcTransaction()) {
            ExecutePaser.parse(this);
        }
        return this.statement.executeUpdate(paramString, paramArrayOfInt);
    }

    @Override
    public int executeUpdate(String paramString, String[] paramArrayOfString) throws SQLException {
        setSql(paramString);
        if (isInTxcTransaction()) {
            ExecutePaser.parse(this);

        }
        return this.statement.executeUpdate(paramString, paramArrayOfString);
    }

    // 不支持
    @Override
    public void closeOnCompletion()
    {
        throw new RuntimeException("Unsupported");
    }

    @Override
    public boolean isCloseOnCompletion()
    {
        throw new RuntimeException("Unsupported");
    }

    // 完全不动直接代理
    @Override
    public void cancel() throws SQLException {
        this.statement.cancel();
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.statement.clearWarnings();
    }

    @Override
    public void close() throws SQLException {
        this.statement.close();
    }
    
    @Override
    public void addBatch(String paramString) throws SQLException {
        this.statement.addBatch(paramString);
    }

    @Override
    public void clearBatch() throws SQLException {
        this.statement.clearBatch();
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return this.statement.getConnection();
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return this.statement.getFetchDirection();
    }

    @Override
    public int getFetchSize() throws SQLException {
        return this.statement.getFetchSize();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return this.statement.getGeneratedKeys();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return this.statement.getMaxFieldSize();
    }

    @Override
    public int getMaxRows() throws SQLException {
        return this.statement.getMaxRows();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return this.statement.getMoreResults();
    }

    @Override
    public boolean getMoreResults(int paramInt) throws SQLException {
        return this.statement.getMoreResults(paramInt);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return this.statement.getQueryTimeout();
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return this.statement.getResultSet();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return this.statement.getResultSetConcurrency();
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return this.statement.getResultSetHoldability();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return this.statement.getResultSetType();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return this.statement.getUpdateCount();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.statement.getWarnings();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.statement.isClosed();
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return this.statement.isPoolable();
    }

    @Override
    public void setCursorName(String paramString) throws SQLException {
        this.statement.setCursorName(paramString);
    }

    @Override
    public void setEscapeProcessing(boolean paramBoolean) throws SQLException {
        this.statement.setEscapeProcessing(paramBoolean);
    }

    @Override
    public void setFetchDirection(int paramInt) throws SQLException {
        this.statement.setFetchDirection(paramInt);
    }

    @Override
    public void setFetchSize(int paramInt) throws SQLException {
        this.statement.setFetchSize(paramInt);
    }

    @Override
    public void setMaxFieldSize(int paramInt) throws SQLException {
        this.statement.setMaxFieldSize(paramInt);
    }

    @Override
    public void setMaxRows(int paramInt) throws SQLException {
        this.statement.setMaxRows(paramInt);
    }

    @Override
    public void setPoolable(boolean paramBoolean) throws SQLException {
        this.statement.setPoolable(paramBoolean);
    }

    @Override
    public void setQueryTimeout(int paramInt) throws SQLException {
        this.statement.setQueryTimeout(paramInt);
    }

    @Override
    public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
        return this.statement.isWrapperFor(paramClass);
    }

    @Override
    public <T> T unwrap(Class<T> paramClass) throws SQLException {
        return this.statement.unwrap(paramClass);
    }

    @Override
    public AbstractTxcConnection getTxcDBConnection() {
        return abstractTxcConnection;
    }
}
