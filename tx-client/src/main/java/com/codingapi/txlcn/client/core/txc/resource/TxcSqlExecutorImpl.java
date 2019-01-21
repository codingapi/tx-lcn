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
package com.codingapi.txlcn.client.core.txc.resource;

import com.codingapi.txlcn.client.core.txc.resource.def.TxcSqlExecutor;
import com.codingapi.txlcn.client.core.txc.resource.def.bean.*;
import com.codingapi.txlcn.client.core.txc.resource.init.TxcSql;
import com.codingapi.txlcn.client.core.txc.resource.rs.UpdateSqlPreDataHandler;
import com.codingapi.txlcn.client.core.txc.resource.util.SqlUtils;
import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description: TXC相关的数据表操作
 * <p>
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Slf4j
public class TxcSqlExecutorImpl implements TxcSqlExecutor {

    private final QueryRunner queryRunner;

    private final TxcSql txcSql;

    private final TxLogger txLogger;


    @Autowired
    public TxcSqlExecutorImpl(QueryRunner queryRunner, TxcSql txcSql, TxLogger txLogger) {
        this.queryRunner = queryRunner;
        this.txcSql = txcSql;
        this.txLogger = txLogger;
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
    public void writeUndoLog(UndoLogDO undoLogDo) throws SQLException {
        Connection connection = queryRunner.getDataSource().getConnection();
        writeUndoLogByGivenConnection(connection, undoLogDo);
        DbUtils.close(connection);
    }

    @Override
    public void writeUndoLogByGivenConnection(Connection connection, UndoLogDO undoLogDo) throws SQLException {
        log.debug("txc > write undo log. params: {}", undoLogDo);
        // 后置镜像查询 暂不记录
        txLogger.trace(undoLogDo.getGroupId(), undoLogDo.getUnitId(), "txc",
                "write undo log before. groupId: " + undoLogDo.getGroupId() +
                        ", unitId: " + undoLogDo.getUnitId());
        // 写
        String undoLogSql = "INSERT INTO `"
                + txcSql.undoLogTableName()
                + "`(gmt_create, gmt_modified, group_id, unit_id, rollback_info) values(?, ?, ?, ?, ?)";
        long count = queryRunner.insert(connection, undoLogSql,
                new ScalarHandler<>(),
                undoLogDo.getGmtCreate(),
                undoLogDo.getGmtModified(),
                undoLogDo.getGroupId(),
                undoLogDo.getUnitId(),
                undoLogDo.getRollbackInfo());
        txLogger.trace(undoLogDo.getGroupId(), undoLogDo.getUnitId(), "txc", "write undo log. log id: " + count);
    }

    @Override
    public void applyUndoLog(String groupId, String unitId) throws SQLException {
        log.debug("txc > execute undo log. groupId: {}, unitId: {}", groupId, unitId);
        String undoLogSql = "SELECT rollback_info FROM `" + txcSql.undoLogTableName() +
                "` WHERE `group_id`=? and `unit_id`=?";
        Connection connection = null;
        try {
            List<StatementInfo> undoLogDOList = queryRunner.query(undoLogSql, rs -> {
                List<StatementInfo> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(SqlUtils.blobToObject(rs.getBytes(1), StatementInfo.class));
                }
                return list;
            }, groupId, unitId);
            txLogger.trace(groupId, unitId, "txc", "undoLogDo sql " + undoLogDOList);
            if (undoLogDOList.isEmpty()) {
                log.warn("txc . undo log not found!");
                txLogger.trace(groupId, unitId, Transactions.TXC, "undo log not found!");
                return;
            }
            RollbackInfo rollbackInfo = new RollbackInfo();
            rollbackInfo.setRollbackSqlList(undoLogDOList);
            txLogger.trace(groupId, unitId, "txc", "rollbackInfo sql " + rollbackInfo.toString());
            connection = queryRunner.getDataSource().getConnection();
            undoRollbackInfoSql(connection, rollbackInfo);
        } finally {
            try {
                DbUtils.close(connection);
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public void applyUndoLog(List<StatementInfo> statementInfoList) throws SQLException {
        Connection connection = null;
        try {
            RollbackInfo rollbackInfo = new RollbackInfo();
            connection = queryRunner.getDataSource().getConnection();
            rollbackInfo.setRollbackSqlList(statementInfoList);
            undoRollbackInfoSql(connection, rollbackInfo);
        } finally {
            DbUtils.close(connection);
        }
    }

    @Override
    public void undoRollbackInfoSql(Connection connection, RollbackInfo rollbackInfo) throws SQLException {
        try {
            connection.setAutoCommit(false);
            for (StatementInfo statementInfo : rollbackInfo.getRollbackSqlList()) {
                log.debug("txc > Apply undo log. sql: {}, params: {}", statementInfo.getSql(), statementInfo.getParams());
                queryRunner.update(connection, statementInfo.getSql(), statementInfo.getParams());
            }
            connection.commit();
        } catch (SQLException e) {
            DbUtils.rollback(connection);
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void clearUndoLog(String groupId, String unitId) throws SQLException {
        log.debug("txc > clear undo log. groupId: {}, unitId: {}", groupId, unitId);
        txLogger.trace(groupId, unitId, "txc", "clear undo log");
        String cleanUndoLogSql = "DELETE FROM `" + txcSql.undoLogTableName() + "` WHERE group_id = ? and unit_id = ?";
        queryRunner.update(cleanUndoLogSql, groupId, unitId);
    }
}
