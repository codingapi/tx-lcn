/**
 * P6Spy
 *
 * Copyright (C) 2002 - 2018 P6Spy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.jdbcproxy.p6spy.event;


import com.codingapi.txlcn.jdbcproxy.p6spy.common.*;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Implementations of this class receive notifications for interesting JDBC events.
 * <p>
 * This class intentionally is not an interface so that methods can be added without breaking existing implementations.
 * <p>
 * There are two ways to register your custom implementation of this class.
 * The fist way is to add the fully qualified class name of your implementation to
 * <code>src/main/resources/META-INF/services/com.p6spy.engine.event.JdbcEventListener</code>.
 * <b>NOTE:</b> Exceptions thrown in this event listener won't be caught. So you have to make sure that your event
 * listener does not throw exceptions. For example, if your {@link #onConnectionWrapped} method throws an exception
 * your application won't be able to create any {@link Connection}.
 */
public abstract class JdbcEventListener {

  /**
   * This callback method is executed before a {@link Connection} obtained from a {@link DataSource} or a {@link Driver}.
   * <p>
   * The {@link ConnectionInformation} holds information about the creator of the connection which is either
   * ,though {@link ConnectionInformation#connection} itself is <code>null</code>.
   *
   * @param connectionInformation The meta information about the wrapped {@link Connection}
   */
  public void onBeforeGetConnection(ConnectionInformation connectionInformation) {
  }

  /**
   * This callback method is executed after a {@link Connection} obtained from a {@link DataSource} or a {@link Driver}.
   * <p>
   * The {@link ConnectionInformation} holds information about the creator of the connection which is either
   * , though {@link ConnectionInformation#connection} itself is <code>null</code>
   * when {@link SQLException} is not <code>null</code> and vise versa.
   *
   * @param connectionInformation The meta information about the wrapped {@link Connection}
   * @param e                     The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                              there was no exception).
   */
  public void onAfterGetConnection(ConnectionInformation connectionInformation, SQLException e) {
  }

  /**
   * This callback method is executed after a wrapped {@link Connection} has been created.
   * <p>
   * The {@link ConnectionInformation} holds information about the creator of the connection which is either
   *
   * @param connectionInformation The meta information about the wrapped {@link Connection}
   *
   * @deprecated Use {@link #onAfterGetConnection(ConnectionInformation, SQLException)}
   */
  @Deprecated
  public void onConnectionWrapped(ConnectionInformation connectionInformation) {
  }

  /**
   * This callback method is executed before the {@link PreparedStatement#addBatch()} method is invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   */
  public void onBeforeAddBatch(PreparedStatementInformation statementInformation) {
  }

  /**
   * This callback method is executed after the {@link Statement#addBatch(String)} or
   * {@link PreparedStatement#addBatch(String)} method is invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param timeElapsedNanos     The execution time of the execute call
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterAddBatch(PreparedStatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
  }

  /**
   * This callback method is executed before the {@link Statement#addBatch(String)} or
   * {@link PreparedStatement#addBatch(String)} method is invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param sql                  The SQL string provided to the execute method
   */
  public void onBeforeAddBatch(StatementInformation statementInformation, String sql) {
  }

  /**
   * This callback method is executed after the {@link Statement#addBatch(String)} or
   * {@link PreparedStatement#addBatch(String)} method is invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param timeElapsedNanos     The execution time of the execute call
   * @param sql                  The SQL string provided to the execute method
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterAddBatch(StatementInformation statementInformation, long timeElapsedNanos, String sql, SQLException e) {
  }


  /**
   * This callback method is executed before any of the {@link PreparedStatement#execute()} methods are invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @throws  SQLException SQLException
   */
  public void onBeforeExecute(PreparedStatementInformation statementInformation)  throws SQLException{
  }

  /**
   * This callback method is executed after any the {@link PreparedStatement#execute()} methods are invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param timeElapsedNanos     The execution time of the execute call
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterExecute(PreparedStatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
  }

  /**
   * This callback method is executed before any of the {@link Statement#execute(String)} methods are invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param sql                  The SQL string provided to the execute method
   * @throws SQLException SQLException
   */
  public void onBeforeExecute(StatementInformation statementInformation, String sql) throws SQLException {
  }

  /**
   * This callback method is executed after any the {@link Statement#execute(String)} methods are invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param timeElapsedNanos     The execution time of the execute call
   * @param sql                  The SQL string provided to the execute method
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterExecute(StatementInformation statementInformation, long timeElapsedNanos, String sql, SQLException e) {
  }


  /**
   * This callback method is executed before the {@link Statement#executeBatch()} method is invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @throws SQLException SQLException
   */
  public void onBeforeExecuteBatch(StatementInformation statementInformation) throws SQLException {
  }

  /**
   * This callback method is executed after the {@link Statement#executeBatch()} method is invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param timeElapsedNanos     The execution time of the execute call
   * @param updateCounts         An array of update counts or null if an exception was thrown
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterExecuteBatch(StatementInformation statementInformation, long timeElapsedNanos, int[] updateCounts, SQLException e) {
  }


  /**
   * This callback method is executed before the {@link PreparedStatement#executeUpdate()} method is invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @throws SQLException SQLException
   */
  public void onBeforeExecuteUpdate(PreparedStatementInformation statementInformation) throws SQLException {
  }

  /**
   * This callback method is executed after the {@link PreparedStatement#executeUpdate()} method is invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param timeElapsedNanos     The execution time of the execute call
   * @param rowCount             Either the row count for SQL Data Manipulation Language (DML) statements or 0 for SQL
   *                             statements that return nothing or if an exception was thrown
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterExecuteUpdate(PreparedStatementInformation statementInformation, long timeElapsedNanos, int rowCount, SQLException e) {
  }

  /**
   * This callback method is executed before any of the {@link Statement#executeUpdate(String)} methods are invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param sql                  The SQL string provided to the execute method
   * @throws SQLException SQLException
   */
  public void onBeforeExecuteUpdate(StatementInformation statementInformation, String sql)  throws SQLException{
  }

  /**
   * This callback method is executed after any of the {@link Statement#executeUpdate(String)} methods are invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param timeElapsedNanos     The execution time of the execute call
   * @param sql                  The SQL string provided to the execute method
   * @param rowCount             Either the row count for SQL Data Manipulation Language (DML) statements or 0 for SQL
   *                             statements that return nothing or if an exception was thrown
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterExecuteUpdate(StatementInformation statementInformation, long timeElapsedNanos, String sql, int rowCount, SQLException e) {
  }


  /**
   * This callback method is executed before the {@link PreparedStatement#executeQuery()} method is invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @throws  SQLException SQLException
   */
  public void onBeforeExecuteQuery(PreparedStatementInformation statementInformation) throws SQLException {
  }

  /**
   * This callback method is executed after the {@link PreparedStatement#executeQuery()} method is invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param timeElapsedNanos     The execution time of the execute call
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterExecuteQuery(PreparedStatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
  }

  /**
   * This callback method is executed before the {@link Statement#executeQuery(String)} method is invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param sql                  The SQL string provided to the execute method
   * @throws SQLException SQLException
   */
  public void onBeforeExecuteQuery(StatementInformation statementInformation, String sql) throws SQLException {
  }

  /**
   * This callback method is executed after the {@link Statement#executeQuery(String)} method is invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param timeElapsedNanos     The execution time of the execute call
   * @param sql                  The SQL string provided to the execute method
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterExecuteQuery(StatementInformation statementInformation, long timeElapsedNanos, String sql, SQLException e) {
  }


  /**
   * This callback method is executed after any of the {@link PreparedStatement}.set* methods are invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param parameterIndex       The first parameter is 1, the second is 2, ...
   * @param value                the column value; if the value is SQL NULL, the value returned is null
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterPreparedStatementSet(PreparedStatementInformation statementInformation, int parameterIndex, Object value, SQLException e) {
  }

  /**
   * This callback method is executed after any of the {@link CallableStatement}.set* methods are invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param parameterName        The name of the parameter
   * @param value                the column value; if the value is SQL NULL, the value returned is null
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterCallableStatementSet(CallableStatementInformation statementInformation, String parameterName, Object value, SQLException e) {
  }

  /**
   * This callback method is executed after the {@link Statement#getResultSet()} method is invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param timeElapsedNanos     The execution time of the execute call
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterGetResultSet(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
  }

  /**
   * This callback method is executed before the {@link ResultSet#next()} method is invoked.
   *
   * @param resultSetInformation The meta information about the {@link ResultSet} being invoked
   */
  public void onBeforeResultSetNext(ResultSetInformation resultSetInformation) {
  }

  /**
   * This callback method is executed after the {@link ResultSet#next()} method is invoked.
   *
   * @param resultSetInformation The meta information about the {@link ResultSet} being invoked
   * @param timeElapsedNanos     The execution time of the execute call
   * @param hasNext              The return value of {@link ResultSet#next()}
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterResultSetNext(ResultSetInformation resultSetInformation, long timeElapsedNanos, boolean hasNext, SQLException e) {
  }

  /**
   * This callback method is executed after the {@link ResultSet#close()} method is invoked.
   *
   * @param resultSetInformation The meta information about the {@link ResultSet} being invoked
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterResultSetClose(ResultSetInformation resultSetInformation, SQLException e) {
  }

  /**
   * This callback method is executed after any of the {@link ResultSet}#get*(String) methods are invoked.
   *
   * @param resultSetInformation The meta information about the {@link ResultSet} being invoked
   * @param columnLabel          The label for the column specified with the SQL AS clause. If the SQL AS clause was
   *                             not specified, then the label is the name of the column
   * @param value                The column value; if the value is SQL NULL, the value returned is null
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterResultSetGet(ResultSetInformation resultSetInformation, String columnLabel, Object value, SQLException e) {
  }

  /**
   * This callback method is executed after any of the {@link ResultSet}#get*(int) methods are invoked.
   *
   * @param resultSetInformation The meta information about the {@link ResultSet} being invoked
   * @param columnIndex          the first column is 1, the second is 2, ...
   * @param value                the column value; if the value is SQL NULL, the value returned is null
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterResultSetGet(ResultSetInformation resultSetInformation, int columnIndex, Object value, SQLException e) {
  }

  /**
   * This callback method is executed before the {@link Connection#commit()} method is invoked.
   *
   * @param connectionInformation The meta information about the {@link Connection} being invoked
   */
  public void onBeforeCommit(ConnectionInformation connectionInformation) {
  }

  /**
   * This callback method is executed after the {@link Connection#commit()} method is invoked.
   *
   * @param connectionInformation The meta information about the {@link Connection} being invoked
   * @param timeElapsedNanos      The execution time of the execute call
   * @param e                     The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                              there was no exception).
   */
  public void onAfterCommit(ConnectionInformation connectionInformation, long timeElapsedNanos, SQLException e) {
  }

  /**
   * This callback method is executed after the {@link Connection#close()} method is invoked.
   *
   * @param connectionInformation The meta information about the {@link Connection} being invoked
   * @param e                     The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                              there was no exception).
   */
  public void onAfterConnectionClose(ConnectionInformation connectionInformation, SQLException e) {
  }

  /**
   * This callback method is executed before the {@link Connection#rollback()} or the {@link
   * Connection#rollback(Savepoint)} method is invoked.
   *
   * @param connectionInformation The meta information about the {@link Connection} being invoked
   */
  public void onBeforeRollback(ConnectionInformation connectionInformation) {
  }

  /**
   * This callback method is executed after the {@link Connection#rollback()} or the {@link
   * Connection#rollback(Savepoint)} method is invoked.
   *
   * @param connectionInformation The meta information about the {@link Connection} being invoked
   * @param timeElapsedNanos      The execution time of the execute call
   * @param e                     The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                              there was no exception).
   */
  public void onAfterRollback(ConnectionInformation connectionInformation, long timeElapsedNanos, SQLException e) {
  }
  
  /**
   * This callback method is executed after the {@link Statement#close()} method is invoked.
   *
   * @param statementInformation The meta information about the {@link Statement} being invoked
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterStatementClose(StatementInformation statementInformation, SQLException e) {
  }

}
