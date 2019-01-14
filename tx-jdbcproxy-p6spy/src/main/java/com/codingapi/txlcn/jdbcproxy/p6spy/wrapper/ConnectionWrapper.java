/**
 * P6Spy
 * <p>
 * Copyright (C) 2002 - 2018 P6Spy
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.jdbcproxy.p6spy.wrapper;


import com.codingapi.txlcn.jdbcproxy.p6spy.common.CallableStatementInformation;
import com.codingapi.txlcn.jdbcproxy.p6spy.common.ConnectionInformation;
import com.codingapi.txlcn.jdbcproxy.p6spy.common.PreparedStatementInformation;
import com.codingapi.txlcn.jdbcproxy.p6spy.common.StatementInformation;
import com.codingapi.txlcn.jdbcproxy.p6spy.event.JdbcEventListener;
import com.codingapi.txlcn.jdbcproxy.p6spy.util.TxcUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * This implementation wraps a {@link Connection}  and notifies a {@link JdbcEventListener}
 * about certain method invocations.
 * <p>
 * This class implements the Wrapper or Decorator pattern. Methods default
 * to calling through to the wrapped request object.
 *
 * @see Connection
 */
@Slf4j
public class ConnectionWrapper extends AbstractWrapper implements Connection {

    private final Connection delegate;
    private final JdbcEventListener jdbcEventListener;
    private final ConnectionInformation connectionInformation;

    public static ConnectionWrapper wrap(Connection delegate, JdbcEventListener eventListener, ConnectionInformation connectionInformation) {
        if (delegate == null) {
            return null;
        }
        final ConnectionWrapper connectionWrapper = new ConnectionWrapper(delegate, eventListener, connectionInformation);
        eventListener.onConnectionWrapped(connectionInformation);
        return connectionWrapper;
    }

    /**
     * Should only be called by {@link #wrap(Connection, JdbcEventListener, ConnectionInformation)}
     * <p>
     * Setting to protected instead of private, so that CGLIB can create a subclass/proxy
     * See also: {@code net.sf.cglib.proxy.Enhancer#filterConstructors} (protectedOk: true)
     *
     *
     * @param delegate   delegate
     * @param jdbcEventListener jdbcEventListener
     * @param connectionInformation connectionInformation
     */
    protected ConnectionWrapper(Connection delegate, JdbcEventListener jdbcEventListener, ConnectionInformation connectionInformation) {
        super(delegate);
        if (delegate == null) {
            throw new NullPointerException("Delegate must not be null");
        }
        this.delegate = delegate;
        this.connectionInformation = connectionInformation;
        this.jdbcEventListener = jdbcEventListener;
    }

    public JdbcEventListener getJdbcEventListener() {
        return this.jdbcEventListener;
    }

    public JdbcEventListener getEventListener() {
        return jdbcEventListener;
    }

    public Connection getDelegate() {
        return delegate;
    }

    public ConnectionInformation getConnectionInformation() {
        return connectionInformation;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return StatementWrapper.wrap(delegate.createStatement(), new StatementInformation(connectionInformation), jdbcEventListener);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return StatementWrapper.wrap(delegate.createStatement(resultSetType, resultSetConcurrency), new StatementInformation(connectionInformation), jdbcEventListener);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return StatementWrapper.wrap(delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability), new StatementInformation(connectionInformation), jdbcEventListener);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return PreparedStatementWrapper.wrap(delegate.prepareStatement(TxcUtils.isTxcSQL(sql) ? TxcUtils.protoSQL(sql) : sql), new PreparedStatementInformation(connectionInformation, sql), jdbcEventListener);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return PreparedStatementWrapper.wrap(delegate.prepareStatement(TxcUtils.isTxcSQL(sql) ? TxcUtils.protoSQL(sql) : sql, resultSetType, resultSetConcurrency), new PreparedStatementInformation(connectionInformation, sql), jdbcEventListener);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return PreparedStatementWrapper.wrap(delegate.prepareStatement(TxcUtils.isTxcSQL(sql) ? TxcUtils.protoSQL(sql) : sql, resultSetType, resultSetConcurrency, resultSetHoldability), new PreparedStatementInformation(connectionInformation, sql), jdbcEventListener);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return PreparedStatementWrapper.wrap(delegate.prepareStatement(TxcUtils.isTxcSQL(sql) ? TxcUtils.protoSQL(sql) : sql, autoGeneratedKeys), new PreparedStatementInformation(connectionInformation, sql), jdbcEventListener);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return PreparedStatementWrapper.wrap(delegate.prepareStatement(TxcUtils.isTxcSQL(sql) ? TxcUtils.protoSQL(sql) : sql, columnIndexes), new PreparedStatementInformation(connectionInformation, sql), jdbcEventListener);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return PreparedStatementWrapper.wrap(delegate.prepareStatement(TxcUtils.isTxcSQL(sql) ? TxcUtils.protoSQL(sql) : sql, columnNames), new PreparedStatementInformation(connectionInformation, sql), jdbcEventListener);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return CallableStatementWrapper.wrap(delegate.prepareCall(TxcUtils.isTxcSQL(sql) ? TxcUtils.protoSQL(sql) : sql, resultSetType, resultSetConcurrency, resultSetHoldability), new CallableStatementInformation(connectionInformation, sql), jdbcEventListener);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return CallableStatementWrapper.wrap(delegate.prepareCall(TxcUtils.isTxcSQL(sql) ? TxcUtils.protoSQL(sql) : sql), new CallableStatementInformation(connectionInformation, sql), jdbcEventListener);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return CallableStatementWrapper.wrap(delegate.prepareCall(TxcUtils.isTxcSQL(sql) ? TxcUtils.protoSQL(sql) : sql, resultSetType, resultSetConcurrency), new CallableStatementInformation(connectionInformation, sql), jdbcEventListener);
    }

    @Override
    public void commit() throws SQLException {
        log.debug("transaction type[txc] proxy connection:{} committed.", this);
        SQLException e = null;
        long start = System.nanoTime();
        try {
            jdbcEventListener.onBeforeCommit(connectionInformation);
            delegate.commit();
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            jdbcEventListener.onAfterCommit(connectionInformation, System.nanoTime() - start, e);
        }
    }

    @Override
    public void rollback() throws SQLException {
        log.debug("transaction type[txc] proxy connection:{} rolled back.", this);
        SQLException e = null;
        long start = System.nanoTime();
        try {
            jdbcEventListener.onBeforeRollback(connectionInformation);
            delegate.rollback();
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            jdbcEventListener.onAfterRollback(connectionInformation, System.nanoTime() - start, e);
        }
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        SQLException e = null;
        long start = System.nanoTime();
        try {
            jdbcEventListener.onBeforeRollback(connectionInformation);
            delegate.rollback(savepoint);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            jdbcEventListener.onAfterRollback(connectionInformation, System.nanoTime() - start, e);
        }
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return delegate.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        delegate.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return delegate.getAutoCommit();
    }

    @Override
    public void close() throws SQLException {
        log.debug("transaction type[txc] proxy connection:{} closed.", this);
        SQLException e = null;
        try {
            delegate.close();
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            jdbcEventListener.onAfterConnectionClose(connectionInformation, e);
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        return delegate.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return delegate.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        delegate.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return delegate.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        delegate.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return delegate.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        delegate.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return delegate.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return delegate.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        delegate.clearWarnings();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return delegate.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        delegate.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        delegate.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return delegate.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return delegate.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return delegate.setSavepoint(name);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        delegate.releaseSavepoint(savepoint);
    }

    @Override
    public Clob createClob() throws SQLException {
        return delegate.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return delegate.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return delegate.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return delegate.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return delegate.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        delegate.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        delegate.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return delegate.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return delegate.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return delegate.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return delegate.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        delegate.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return delegate.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        delegate.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        delegate.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return delegate.getNetworkTimeout();
    }

}
