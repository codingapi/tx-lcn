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
package com.codingapi.txlcn.tc.core.transaction.txc.resource;


import com.codingapi.txlcn.tc.support.p6spy.common.*;
import com.codingapi.txlcn.tc.support.p6spy.event.DefaultEventListener;
import com.codingapi.txlcn.tc.support.p6spy.event.JdbcEventListener;
import com.codingapi.txlcn.tc.support.p6spy.event.P6spyJdbcEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class CompoundJdbcEventListener extends JdbcEventListener {

    private final P6spyJdbcEventListener p6spyEventListener;

    @Autowired
    public CompoundJdbcEventListener(P6spyJdbcEventListener p6spyEventListener) {
        this.p6spyEventListener = p6spyEventListener;
    }


    @Override
    public void onBeforeGetConnection(ConnectionInformation connectionInformation) {
        DefaultEventListener.INSTANCE.onBeforeGetConnection(connectionInformation);
        p6spyEventListener.onBeforeGetConnection(connectionInformation);
    }

    @Override
    public void onAfterGetConnection(ConnectionInformation connectionInformation, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterGetConnection(connectionInformation, e);
        p6spyEventListener.onAfterGetConnection(connectionInformation, e);
    }

    @Override
    @Deprecated
    public void onConnectionWrapped(ConnectionInformation connectionInformation) {
        DefaultEventListener.INSTANCE.onConnectionWrapped(connectionInformation);
        p6spyEventListener.onConnectionWrapped(connectionInformation);
    }

    @Override
    public void onBeforeAddBatch(PreparedStatementInformation statementInformation) {
        DefaultEventListener.INSTANCE.onBeforeAddBatch(statementInformation);
        p6spyEventListener.onBeforeAddBatch(statementInformation);
    }

    @Override
    public void onAfterAddBatch(PreparedStatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterAddBatch(statementInformation, timeElapsedNanos, e);
        p6spyEventListener.onAfterAddBatch(statementInformation, timeElapsedNanos, e);
    }

    @Override
    public String onBeforeAddBatch(StatementInformation statementInformation, String sql) {
        DefaultEventListener.INSTANCE.onBeforeAddBatch(statementInformation, sql);
        return p6spyEventListener.onBeforeAddBatch(statementInformation, sql);
    }

    @Override
    public void onAfterAddBatch(StatementInformation statementInformation, long timeElapsedNanos, String sql, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterAddBatch(statementInformation, timeElapsedNanos, sql, e);
        p6spyEventListener.onAfterAddBatch(statementInformation, timeElapsedNanos, sql, e);
    }

    @Override
    public void onBeforeExecute(PreparedStatementInformation statementInformation) throws SQLException {
        DefaultEventListener.INSTANCE.onBeforeExecute(statementInformation);
        p6spyEventListener.onBeforeExecute(statementInformation);
    }

    @Override
    public void onAfterExecute(PreparedStatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterExecute(statementInformation, timeElapsedNanos, e);
        p6spyEventListener.onAfterExecute(statementInformation, timeElapsedNanos, e);
    }

    @Override
    public String onBeforeExecute(StatementInformation statementInformation, String sql) throws SQLException {
        DefaultEventListener.INSTANCE.onBeforeExecute(statementInformation, sql);
        return p6spyEventListener.onBeforeExecute(statementInformation, sql);
    }

    @Override
    public void onAfterExecute(StatementInformation statementInformation, long timeElapsedNanos, String sql, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterExecute(statementInformation, timeElapsedNanos, sql, e);
        p6spyEventListener.onAfterExecute(statementInformation, timeElapsedNanos, sql, e);
    }

    @Override
    public void onBeforeExecuteBatch(StatementInformation statementInformation) throws SQLException {
        DefaultEventListener.INSTANCE.onBeforeExecuteBatch(statementInformation);
        p6spyEventListener.onBeforeExecuteBatch(statementInformation);
    }

    @Override
    public void onAfterExecuteBatch(StatementInformation statementInformation, long timeElapsedNanos, int[] updateCounts, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterExecuteBatch(statementInformation, timeElapsedNanos, updateCounts, e);
        p6spyEventListener.onAfterExecuteBatch(statementInformation, timeElapsedNanos, updateCounts, e);
    }

    @Override
    public void onBeforeExecuteUpdate(PreparedStatementInformation statementInformation) throws SQLException {
        DefaultEventListener.INSTANCE.onBeforeExecuteUpdate(statementInformation);
        p6spyEventListener.onBeforeExecuteUpdate(statementInformation);
    }

    @Override
    public void onAfterExecuteUpdate(PreparedStatementInformation statementInformation, long timeElapsedNanos, int rowCount, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterExecuteUpdate(statementInformation, timeElapsedNanos, rowCount, e);
        p6spyEventListener.onAfterExecuteUpdate(statementInformation, timeElapsedNanos, rowCount, e);
    }

    @Override
    public String onBeforeExecuteUpdate(StatementInformation statementInformation, String sql) throws SQLException {
        DefaultEventListener.INSTANCE.onBeforeExecuteUpdate(statementInformation, sql);
        return p6spyEventListener.onBeforeExecuteUpdate(statementInformation, sql);
    }

    @Override
    public void onAfterExecuteUpdate(StatementInformation statementInformation, long timeElapsedNanos, String sql, int rowCount, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterExecuteUpdate(statementInformation, timeElapsedNanos, sql, rowCount, e);
        p6spyEventListener.onAfterExecuteUpdate(statementInformation, timeElapsedNanos, sql, rowCount, e);
    }

    @Override
    public void onBeforeExecuteQuery(PreparedStatementInformation statementInformation) throws SQLException {
        DefaultEventListener.INSTANCE.onBeforeExecuteQuery(statementInformation);
        p6spyEventListener.onBeforeExecuteQuery(statementInformation);
    }

    @Override
    public void onAfterExecuteQuery(PreparedStatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterExecuteQuery(statementInformation, timeElapsedNanos, e);
        p6spyEventListener.onAfterExecuteQuery(statementInformation, timeElapsedNanos, e);
    }

    @Override
    public String onBeforeExecuteQuery(StatementInformation statementInformation, String sql) throws SQLException {
        DefaultEventListener.INSTANCE.onBeforeExecuteQuery(statementInformation, sql);
        return p6spyEventListener.onBeforeExecuteQuery(statementInformation, sql);
    }

    @Override
    public void onAfterExecuteQuery(StatementInformation statementInformation, long timeElapsedNanos, String sql, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterExecuteQuery(statementInformation, timeElapsedNanos, sql, e);
        p6spyEventListener.onAfterExecuteQuery(statementInformation, timeElapsedNanos, sql, e);
    }

    @Override
    public void onAfterPreparedStatementSet(PreparedStatementInformation statementInformation, int parameterIndex, Object value, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterPreparedStatementSet(statementInformation, parameterIndex, value, e);
        p6spyEventListener.onAfterPreparedStatementSet(statementInformation, parameterIndex, value, e);
    }

    @Override
    public void onAfterCallableStatementSet(CallableStatementInformation statementInformation, String parameterName, Object value, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterCallableStatementSet(statementInformation, parameterName, value, e);
        p6spyEventListener.onAfterCallableStatementSet(statementInformation, parameterName, value, e);
    }

    @Override
    public void onAfterGetResultSet(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterGetResultSet(statementInformation, timeElapsedNanos, e);
        p6spyEventListener.onAfterGetResultSet(statementInformation, timeElapsedNanos, e);
    }

    @Override
    public void onBeforeResultSetNext(ResultSetInformation resultSetInformation) {
        DefaultEventListener.INSTANCE.onBeforeResultSetNext(resultSetInformation);
        p6spyEventListener.onBeforeResultSetNext(resultSetInformation);
    }

    @Override
    public void onAfterResultSetNext(ResultSetInformation resultSetInformation, long timeElapsedNanos, boolean hasNext, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterResultSetNext(resultSetInformation, timeElapsedNanos, hasNext, e);
        p6spyEventListener.onAfterResultSetNext(resultSetInformation, timeElapsedNanos, hasNext, e);
    }

    @Override
    public void onAfterResultSetClose(ResultSetInformation resultSetInformation, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterResultSetClose(resultSetInformation, e);
        p6spyEventListener.onAfterResultSetClose(resultSetInformation, e);
    }

    @Override
    public void onAfterResultSetGet(ResultSetInformation resultSetInformation, String columnLabel, Object value, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterResultSetGet(resultSetInformation, columnLabel, value, e);
        p6spyEventListener.onAfterResultSetGet(resultSetInformation, columnLabel, value, e);
    }

    @Override
    public void onAfterResultSetGet(ResultSetInformation resultSetInformation, int columnIndex, Object value, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterResultSetGet(resultSetInformation, columnIndex, value, e);
        p6spyEventListener.onAfterResultSetGet(resultSetInformation, columnIndex, value, e);
    }

    @Override
    public void onBeforeCommit(ConnectionInformation connectionInformation) {
        DefaultEventListener.INSTANCE.onBeforeCommit(connectionInformation);
        p6spyEventListener.onBeforeCommit(connectionInformation);
    }

    @Override
    public void onAfterCommit(ConnectionInformation connectionInformation, long timeElapsedNanos, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterCommit(connectionInformation, timeElapsedNanos, e);
        p6spyEventListener.onAfterCommit(connectionInformation, timeElapsedNanos, e);
    }

    @Override
    public void onAfterConnectionClose(ConnectionInformation connectionInformation, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterConnectionClose(connectionInformation, e);
        p6spyEventListener.onAfterConnectionClose(connectionInformation, e);
    }

    @Override
    public void onBeforeRollback(ConnectionInformation connectionInformation) {
        DefaultEventListener.INSTANCE.onBeforeRollback(connectionInformation);
        p6spyEventListener.onBeforeRollback(connectionInformation);
    }

    @Override
    public void onAfterRollback(ConnectionInformation connectionInformation, long timeElapsedNanos, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterRollback(connectionInformation, timeElapsedNanos, e);
        p6spyEventListener.onAfterRollback(connectionInformation, timeElapsedNanos, e);
    }

    @Override
    public void onAfterStatementClose(StatementInformation statementInformation, SQLException e) {
        DefaultEventListener.INSTANCE.onAfterStatementClose(statementInformation, e);
        p6spyEventListener.onAfterStatementClose(statementInformation, e);
    }
}
