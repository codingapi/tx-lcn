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


import com.codingapi.txlcn.jdbcproxy.p6spy.common.PreparedStatementInformation;
import com.codingapi.txlcn.jdbcproxy.p6spy.common.ResultSetInformation;
import com.codingapi.txlcn.jdbcproxy.p6spy.event.JdbcEventListener;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

/**
 * This implementation wraps a {@link PreparedStatement}  and notifies a {@link JdbcEventListener}
 * about certain method invocations.
 * <p>
 * This class implements the Wrapper or Decorator pattern. Methods default
 * to calling through to the wrapped request object.
 *
 * @see PreparedStatement
 */
public class PreparedStatementWrapper extends StatementWrapper implements PreparedStatement {

    private final PreparedStatement delegate;
    private final PreparedStatementInformation statementInformation;

    public static PreparedStatement wrap(PreparedStatement delegate, PreparedStatementInformation preparedStatementInformation, JdbcEventListener eventListener) {
        if (delegate == null) {
            return null;
        }
        return new PreparedStatementWrapper(delegate, preparedStatementInformation, eventListener);
    }

    protected PreparedStatementWrapper(PreparedStatement delegate, PreparedStatementInformation preparedStatementInformation, JdbcEventListener eventListener) {
        super(delegate, preparedStatementInformation, eventListener);
        this.delegate = delegate;
        statementInformation = preparedStatementInformation;
        statementInformation.setStatement(this);
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        SQLException e = null;
        long start = System.nanoTime();
        ResultSet bakResultSet;
        try {
            eventListener.onBeforeExecuteQuery(statementInformation);
            return ResultSetWrapper.wrap(delegate.executeQuery(), new ResultSetInformation(statementInformation), eventListener);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterExecuteQuery(statementInformation, System.nanoTime() - start, e);
        }
    }

    @Override
    public int executeUpdate() throws SQLException {
        SQLException e = null;
        long start = System.nanoTime();
        int rowCount = 0;
        try {
            eventListener.onBeforeExecuteUpdate(statementInformation);
            rowCount = delegate.executeUpdate();
            return rowCount;
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterExecuteUpdate(statementInformation, System.nanoTime() - start, rowCount, e);
        }
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNull(parameterIndex, sqlType);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, null, e);
        }
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBoolean(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setByte(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setShort(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setInt(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setLong(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setFloat(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setDouble(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBigDecimal(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setString(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBytes(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setDate(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setTime(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setTimestamp(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setAsciiStream(parameterIndex, x, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setUnicodeStream(parameterIndex, x, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBinaryStream(parameterIndex, x, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void clearParameters() throws SQLException {
        delegate.clearParameters();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        SQLException e = null;
        try {
            delegate.setObject(parameterIndex, x, targetSqlType);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setObject(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public boolean execute() throws SQLException {
        SQLException e = null;
        long start = System.nanoTime();
        try {
            eventListener.onBeforeExecute(statementInformation);
            return delegate.execute();
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterExecute(statementInformation, System.nanoTime() - start, e);
        }
    }

    @Override
    public void addBatch() throws SQLException {
        SQLException e = null;
        long start = System.nanoTime();
        try {
            eventListener.onBeforeAddBatch(statementInformation);
            delegate.addBatch();
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterAddBatch(statementInformation, System.nanoTime() - start, e);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setCharacterStream(parameterIndex, reader, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, reader, e);
        }
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setRef(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBlob(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setClob(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setArray(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        SQLException e = null;
        try {
            delegate.setDate(parameterIndex, x, cal);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        SQLException e = null;
        try {
            delegate.setTime(parameterIndex, x, cal);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        SQLException e = null;
        try {
            delegate.setTimestamp(parameterIndex, x, cal);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNull(parameterIndex, sqlType, typeName);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, null, e);
        }
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setURL(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setRowId(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNString(parameterIndex, value);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, value, e);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNCharacterStream(parameterIndex, value, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, value, e);
        }
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNClob(parameterIndex, value);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, value, e);
        }
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setClob(parameterIndex, reader, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, reader, e);
        }
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBlob(parameterIndex, inputStream, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, inputStream, e);
        }
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNClob(parameterIndex, reader, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, reader, e);
        }
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        SQLException e = null;
        try {
            delegate.setSQLXML(parameterIndex, xmlObject);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, xmlObject, e);
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        SQLException e = null;
        try {
            delegate.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setAsciiStream(parameterIndex, x, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBinaryStream(parameterIndex, x, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        SQLException e = null;
        try {
            delegate.setCharacterStream(parameterIndex, reader, length);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, reader, e);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setAsciiStream(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBinaryStream(parameterIndex, x);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, x, e);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        SQLException e = null;
        try {
            delegate.setCharacterStream(parameterIndex, reader);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, reader, e);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNCharacterStream(parameterIndex, value);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, value, e);
        }
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        SQLException e = null;
        try {
            delegate.setClob(parameterIndex, reader);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, reader, e);
        }
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        SQLException e = null;
        try {
            delegate.setBlob(parameterIndex, inputStream);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, inputStream, e);
        }
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        SQLException e = null;
        try {
            delegate.setNClob(parameterIndex, reader);
        } catch (SQLException sqle) {
            e = sqle;
            throw e;
        } finally {
            eventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, reader, e);
        }
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return delegate.getParameterMetaData();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return delegate.getMetaData();
    }

}
