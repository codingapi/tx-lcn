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




import com.codingapi.txlcn.jdbcproxy.p6spy.common.PreparedStatementInformation;
import com.codingapi.txlcn.jdbcproxy.p6spy.common.StatementInformation;

import java.sql.SQLException;

/**
 * This event listener offers more coarse grained event listener methods as it aggregates events for the execute* and
 * addBatch methods.
 */
public abstract class SimpleJdbcEventListener extends JdbcEventListener {

  /**
   * This callback method is executed before any {@link java.sql.Statement}.execute* method is invoked
   *
   * @param statementInformation The meta information about the {@link java.sql.Statement} being invoked
   * @throws SQLException SQLException
   */
  public void onBeforeAnyExecute(StatementInformation statementInformation) throws SQLException {
  }

  /**
   * This callback method is executed after any {@link java.sql.Statement}.execute* method is invoked
   *
   * @param statementInformation The meta information about the {@link java.sql.Statement} being invoked
   * @param timeElapsedNanos     The execution time of the execute call
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterAnyExecute(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
  }

  /**
   * This callback method is executed before any {@link java.sql.Statement}.addBatch* method is invoked
   *
   * @param statementInformation The meta information about the {@link java.sql.Statement} being invoked
   */
  public void onBeforeAnyAddBatch(StatementInformation statementInformation) {
  }

  /**
   * This callback method is executed before any {@link java.sql.Statement}.addBatch* method is invoked
   *
   * @param statementInformation The meta information about the {@link java.sql.Statement} being invoked
   * @param timeElapsedNanos     The execution time of the execute call
   * @param e                    The {@link SQLException} which may be triggered by the call (<code>null</code> if
   *                             there was no exception).
   */
  public void onAfterAnyAddBatch(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
  }

  @Override
  public void onBeforeExecute(PreparedStatementInformation statementInformation)  throws SQLException {
    onBeforeAnyExecute(statementInformation);
  }

  @Override
  public void onBeforeExecute(StatementInformation statementInformation, String sql)  throws SQLException{
    onBeforeAnyExecute(statementInformation);
  }

  @Override
  public void onBeforeExecuteBatch(StatementInformation statementInformation) throws SQLException {
    onBeforeAnyExecute(statementInformation);
  }

  @Override
  public void onBeforeExecuteUpdate(PreparedStatementInformation statementInformation)  throws SQLException{
    onBeforeAnyExecute(statementInformation);
  }

  @Override
  public void onBeforeExecuteUpdate(StatementInformation statementInformation, String sql) throws SQLException {
    onBeforeAnyExecute(statementInformation);
  }

  @Override
  public void onBeforeExecuteQuery(PreparedStatementInformation statementInformation) throws SQLException {
    onBeforeAnyExecute(statementInformation);
  }

  @Override
  public void onBeforeExecuteQuery(StatementInformation statementInformation, String sql) throws SQLException {
    onBeforeAnyExecute(statementInformation);
  }

  @Override
  public void onAfterExecute(PreparedStatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
    onAfterAnyExecute(statementInformation, timeElapsedNanos, e);
  }

  @Override
  public void onAfterExecute(StatementInformation statementInformation, long timeElapsedNanos, String sql, SQLException e) {
    onAfterAnyExecute(statementInformation, timeElapsedNanos, e);
  }

  @Override
  public void onAfterExecuteBatch(StatementInformation statementInformation, long timeElapsedNanos, int[] updateCounts, SQLException e) {
    onAfterAnyExecute(statementInformation, timeElapsedNanos, e);
  }

  @Override
  public void onAfterExecuteUpdate(PreparedStatementInformation statementInformation, long timeElapsedNanos, int rowCount, SQLException e) {
    onAfterAnyExecute(statementInformation, timeElapsedNanos, e);
  }

  @Override
  public void onAfterExecuteUpdate(StatementInformation statementInformation, long timeElapsedNanos, String sql, int rowCount, SQLException e) {
    onAfterAnyExecute(statementInformation, timeElapsedNanos, e);
  }

  @Override
  public void onAfterExecuteQuery(PreparedStatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
    onAfterAnyExecute(statementInformation, timeElapsedNanos, e);
  }

  @Override
  public void onAfterExecuteQuery(StatementInformation statementInformation, long timeElapsedNanos, String sql, SQLException e) {
    onAfterAnyExecute(statementInformation, timeElapsedNanos, e);
  }

  @Override
  public void onBeforeAddBatch(PreparedStatementInformation statementInformation) {
    onBeforeAnyAddBatch(statementInformation);
  }

  @Override
  public void onBeforeAddBatch(StatementInformation statementInformation, String sql) {
    onBeforeAnyAddBatch(statementInformation);
  }

  @Override
  public void onAfterAddBatch(PreparedStatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
    onAfterAnyAddBatch(statementInformation, timeElapsedNanos, e);
  }

  @Override
  public void onAfterAddBatch(StatementInformation statementInformation, long timeElapsedNanos, String sql, SQLException e) {
    onAfterAnyAddBatch(statementInformation, timeElapsedNanos, e);
  }
}
