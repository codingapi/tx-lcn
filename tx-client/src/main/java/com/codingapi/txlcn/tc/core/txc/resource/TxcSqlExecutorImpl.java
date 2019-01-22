/*
 * Copyright 2017-2019 CodingApi .
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
package com.codingapi.txlcn.tc.core.txc.resource;

import com.codingapi.txlcn.tc.core.txc.resource.def.TxcSqlExecutor;
import com.codingapi.txlcn.tc.core.txc.resource.def.bean.*;
import com.codingapi.txlcn.tc.core.txc.resource.init.TxcSql;
import com.codingapi.txlcn.tc.core.txc.resource.rs.UpdateSqlPreDataHandler;
import com.codingapi.txlcn.tc.core.txc.resource.util.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Description: TXC相关的数据表操作
 * <p>
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component
@Slf4j
public class TxcSqlExecutorImpl implements TxcSqlExecutor {

    private final QueryRunner queryRunner;

    private final TxcSql txcSql;

    public TxcSqlExecutorImpl(QueryRunner queryRunner, TxcSql txcSql) {
        this.queryRunner = queryRunner;
        this.txcSql = txcSql;
    }


    @Override
    public void createLockTable() {
        try {
            queryRunner.execute(txcSql.lockTableSql());
        } catch (SQLException e) {
            log.error("txc > sql executor > create lock table error.", e);
        }
    }

    @Override
    public void createUndoLogTable() {
        try {
            queryRunner.execute(txcSql.undoLogTableSql());
        } catch (SQLException e) {
            log.error("txc > sql executor > create undo_log table error.", e);
        }
    }

    @Override
    public List<ModifiedRecord> updateSqlPreviousData(Connection connection, UpdateImageParams updateImageParams)
            throws SQLException {
        // 前置镜像sql
        String beforeSql = SqlUtils.SELECT
                + String.join(SqlUtils.SQL_COMMA_SEPARATOR, updateImageParams.getColumns())
                + SqlUtils.SQL_COMMA_SEPARATOR
                + String.join(SqlUtils.SQL_COMMA_SEPARATOR, updateImageParams.getPrimaryKeys())
                + SqlUtils.FROM
                + String.join(SqlUtils.SQL_COMMA_SEPARATOR, updateImageParams.getTables())
                + SqlUtils.WHERE
                + updateImageParams.getWhereSql();
        return queryRunner.query(connection, beforeSql,
                new UpdateSqlPreDataHandler(updateImageParams.getPrimaryKeys(), updateImageParams.getColumns()));
    }

    @Override
    public List<ModifiedRecord> deleteSqlPreviousData(Connection connection, DeleteImageParams deleteImageParams)
            throws SQLException {
        String beforeSql = SqlUtils.SELECT + String.join(SqlUtils.SQL_COMMA_SEPARATOR, deleteImageParams.getColumns()) +
                SqlUtils.FROM +
                String.join(SqlUtils.SQL_COMMA_SEPARATOR, deleteImageParams.getTables()) +
                SqlUtils.WHERE +
                deleteImageParams.getSqlWhere();
        return queryRunner.query(connection, beforeSql,
                new UpdateSqlPreDataHandler(
                        deleteImageParams.getPrimaryKeys(),
                        deleteImageParams.getColumns()));
    }

    @Override
    public List<ModifiedRecord> selectSqlPreviousPrimaryKeys(Connection connection, SelectImageParams selectImageParams)
            throws SQLException {
        return queryRunner.query(connection, selectImageParams.getSql(),
                new UpdateSqlPreDataHandler(
                        selectImageParams.getPrimaryKeys(),
                        selectImageParams.getPrimaryKeys()));
    }

    @Override
    public void tryLock(Connection connection, LockInfo lockInfo) throws SQLException {
        String lockSql = "INSERT INTO `" + txcSql.lockTableName() +
                "` (table_name, key_value, group_id, unit_id, x_lock, s_lock) values(?, ?, ?, ?, ?, ?)";
        queryRunner.insert(connection, lockSql, new ScalarHandler<Integer>(),
                lockInfo.getTableName(),
                lockInfo.getKeyValue(),
                lockInfo.getGroupId(),
                lockInfo.getUnitId(),
                lockInfo.isXLock() ? 1 : null,
                lockInfo.isXLock() ? null : 1);
    }

    @Override
    public void clearLock(String groupId, String unitId) throws SQLException {
        log.debug("txc > sql > executor > clear lock. groupId: {}, unitId: {}", groupId, unitId);
        String cleanLockSql = "DELETE FROM `" + txcSql.lockTableName() + "` where group_id = ? and unit_id = ?";
        queryRunner.update(cleanLockSql, groupId, unitId);

    }

    @Override
    public void applyUndoLog(List<StatementInfo> statementInfoList) throws SQLException {
        Connection connection = null;
        try {
            connection = queryRunner.getDataSource().getConnection();
            connection.setAutoCommit(false);
            for (StatementInfo statementInfo : statementInfoList) {
                log.debug("txc > Apply undo log. sql: {}, params: {}", statementInfo.getSql(), statementInfo.getParams());
                queryRunner.update(connection, statementInfo.getSql(), statementInfo.getParams());
            }
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                DbUtils.close(connection);
            }
        }
    }
}
