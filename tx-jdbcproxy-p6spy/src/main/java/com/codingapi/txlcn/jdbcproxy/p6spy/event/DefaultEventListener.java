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



import com.codingapi.txlcn.jdbcproxy.p6spy.common.CallableStatementInformation;
import com.codingapi.txlcn.jdbcproxy.p6spy.common.PreparedStatementInformation;
import com.codingapi.txlcn.jdbcproxy.p6spy.common.ResultSetInformation;
import com.codingapi.txlcn.jdbcproxy.p6spy.common.StatementInformation;

import java.sql.SQLException;

/**
 * This implementation of {@link JdbcEventListener} must always be applied as the first listener.
 * It populates the information objects {@link StatementInformation}, {@link PreparedStatementInformation},
 *  {@link ResultSetInformation}
 */
public class DefaultEventListener extends JdbcEventListener {

  public static final DefaultEventListener INSTANCE = new DefaultEventListener();

  private DefaultEventListener() {
  }

  @Override
  public void onAfterAddBatch(StatementInformation statementInformation, long timeElapsedNanos, String sql, SQLException e) {
  }

  @Override
  public void onAfterExecute(PreparedStatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
    statementInformation.incrementTimeElapsed(timeElapsedNanos);
  }

  @Override
  public void onAfterExecute(StatementInformation statementInformation, long timeElapsedNanos, String sql, SQLException e) {
    statementInformation.incrementTimeElapsed(timeElapsedNanos);
  }

  @Override
  public void onAfterExecuteBatch(StatementInformation statementInformation, long timeElapsedNanos, int[] updateCounts, SQLException e) {
    statementInformation.incrementTimeElapsed(timeElapsedNanos);
  }

  @Override
  public void onAfterExecuteUpdate(PreparedStatementInformation statementInformation, long timeElapsedNanos, int rowCount, SQLException e) {
    statementInformation.incrementTimeElapsed(timeElapsedNanos);
  }

  @Override
  public void onAfterExecuteUpdate(StatementInformation statementInformation, long timeElapsedNanos, String sql, int rowCount, SQLException e) {
    statementInformation.incrementTimeElapsed(timeElapsedNanos);
  }

  @Override
  public void onAfterExecuteQuery(PreparedStatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
    statementInformation.incrementTimeElapsed(timeElapsedNanos);
  }

  @Override
  public void onAfterExecuteQuery(StatementInformation statementInformation, long timeElapsedNanos, String sql, SQLException e) {
    statementInformation.incrementTimeElapsed(timeElapsedNanos);
  }

  @Override
  public void onAfterGetResultSet(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
    statementInformation.incrementTimeElapsed(timeElapsedNanos);
  }

  @Override
  public void onAfterResultSetNext(ResultSetInformation resultSetInformation, long timeElapsedNanos, boolean hasNext, SQLException e) {
    resultSetInformation.getStatementInformation().incrementTimeElapsed(timeElapsedNanos);
    if (hasNext) {
      resultSetInformation.incrementCurrRow();
    }
  }

  @Override
  public void onAfterCallableStatementSet(CallableStatementInformation statementInformation, String parameterName, Object value, SQLException e) {
    statementInformation.setParameterValue(parameterName, value);
  }

  @Override
  public void onAfterPreparedStatementSet(PreparedStatementInformation statementInformation, int parameterIndex, Object value, SQLException e) {
    statementInformation.setParameterValue(parameterIndex, value);
  }

}
