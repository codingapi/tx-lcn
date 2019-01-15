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
import com.codingapi.txlcn.client.core.txc.resource.init.TxcSettingFactory;
import com.codingapi.txlcn.client.core.txc.resource.rs.UpdateSqlPreDataHandler;
import com.codingapi.txlcn.client.core.txc.resource.util.SqlUtils;
import com.codingapi.txlcn.jdbcproxy.p6spy.util.TxcUtils;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.client.core.txc.resource.def.bean.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

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

    private final TxcSettingFactory txcSettingFactory;

    private final TxLogger txLogger;

    @Autowired
    public TxcSqlExecutorImpl(QueryRunner queryRunner, TxcSettingFactory txcSettingFactory, TxLogger txLogger) {
        this.queryRunner = queryRunner;
        this.txcSettingFactory = txcSettingFactory;
        this.txLogger = txLogger;
    }

    @Override
    public void createLockTable() {
        try {
            queryRunner.execute(txcSettingFactory.lockTableSql());
        } catch (SQLException e) {
            log.error("txc > sql executor > create lock table error.", e);
        }
    }

    @Override
    public void createUndoLogTable() {
        try {
            queryRunner.execute(txcSettingFactory.undoLogTableSql());
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
        return queryRunner.query(connection, TxcUtils.txcSQL(beforeSql),
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
        return queryRunner.query(connection, TxcUtils.txcSQL(beforeSql),
                new UpdateSqlPreDataHandler(
                        deleteImageParams.getPrimaryKeys(),
                        deleteImageParams.getColumns()));
    }

    @Override
    public List<ModifiedRecord> selectSqlPreviousPrimaryKeys(Connection connection, SelectImageParams selectImageParams)
            throws SQLException {
        return queryRunner.query(connection, TxcUtils.txcSQL(selectImageParams.getSql()),
                new UpdateSqlPreDataHandler(
                        selectImageParams.getPrimaryKeys(),
                        selectImageParams.getPrimaryKeys()));
    }

    @Override
    public void tryLock(Connection connection, LockInfo lockInfo) throws SQLException {
        String lockSql = "INSERT INTO `" + txcSettingFactory.lockTableName() +
                "` (table_name, key_value, group_id, unit_id, x_lock, s_lock) values(?, ?, ?, ?, ?, ?)";
        queryRunner.insert(connection, TxcUtils.txcSQL(lockSql), new ScalarHandler<Integer>(),
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
        String cleanLockSql = "DELETE FROM `" + txcSettingFactory.lockTableName() + "` where group_id = ? and unit_id = ?";
        queryRunner.update(cleanLockSql, groupId, unitId);

    }

    @Override
    public void writeUndoLog(UndoLogDO undoLogDo) throws SQLException {
        log.debug("txc > write undo log. params: {}", undoLogDo);
        // 后置镜像查询 暂不记录

        // 写
        String undoLogSql = "INSERT INTO `"
                + txcSettingFactory.undoLogTableName()
                + "`(gmt_create, gmt_modified, group_id, unit_id, rollback_info) values(?, ?, ?, ?, ?)";
        long count = queryRunner.insert(undoLogSql,
                new ScalarHandler<>(),
                undoLogDo.getGmtCreate(),
                undoLogDo.getGmtModified(),
                undoLogDo.getGroupId(),
                undoLogDo.getUnitId(),
                undoLogDo.getRollbackInfo());
        txLogger.trace(undoLogDo.getGroupId(), undoLogDo.getUnitId(), "txc", "write undo log " + count);
    }

    @Override
    public void applyUndoLog(String groupId, String unitId) throws SQLException {
        log.debug("txc > execute undo log. groupId: {}, unitId: {}", groupId, unitId);
        String undoLogSql = "SELECT * FROM `" + txcSettingFactory.undoLogTableName() +
                "` WHERE `group_id`=? and `unit_id`=?";
        Connection connection = null;
        try {
            BeanProcessor bean = new GenerousBeanProcessor();
            RowProcessor processor = new BasicRowProcessor(bean);
            UndoLogDO undoLogDo = queryRunner.query(undoLogSql, new BeanHandler<>(UndoLogDO.class, processor), groupId, unitId);
            txLogger.trace(groupId, unitId, "txc", "undoLogDo sql " + undoLogDo);
            if (Objects.isNull(undoLogDo)) {
                log.warn("txc . undo log not found! if in 'the ex caused mod' can be ignored.");
                return;
            }
            RollbackInfo rollbackInfo = SqlUtils.blobToObject(undoLogDo.getRollbackInfo(), RollbackInfo.class);
            txLogger.trace(groupId, unitId, "txc", "rollbackInfo sql " + rollbackInfo.toString());
            connection = queryRunner.getDataSource().getConnection();
            undoRollbackInfoSql(connection, rollbackInfo);
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                DbUtils.close(connection);
            } catch (SQLException ignored) {
            }
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
        String cleanUndoLogSql = "DELETE FROM `" + txcSettingFactory.undoLogTableName() + "` WHERE group_id = ? and unit_id = ?";
        queryRunner.update(cleanUndoLogSql, groupId, unitId);
    }
}
