package com.codingapi.tx.client.spi.transaction.txc.resource;

import com.codingapi.tx.client.spi.transaction.txc.resource.def.TxcSqlExecutor;
import com.codingapi.tx.client.spi.transaction.txc.resource.def.bean.*;
import com.codingapi.tx.client.spi.transaction.txc.resource.rs.UpdateSqlPreDataHandler;
import com.codingapi.tx.client.spi.transaction.txc.resource.init.TxcSettingFactory;
import com.codingapi.tx.client.spi.transaction.txc.resource.util.SqlUtils;
import com.codingapi.tx.logger.TxLogger;
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

    @Autowired
    private TxLogger txLogger;

    @Autowired
    public TxcSqlExecutorImpl(QueryRunner queryRunner, TxcSettingFactory txcSettingFactory) {
        this.queryRunner = queryRunner;
        this.txcSettingFactory = txcSettingFactory;
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
    public List<ModifiedRecord> updateSqlPreviousData(UpdateImageParams updateImageParams) {
        // 前置镜像sql
        String beforeSql = SqlUtils.SELECT
                + String.join(SqlUtils.SQL_COMMA_SEPARATOR, updateImageParams.getColumns())
                + SqlUtils.SQL_COMMA_SEPARATOR
                + String.join(SqlUtils.SQL_COMMA_SEPARATOR, updateImageParams.getPrimaryKeys())
                + SqlUtils.FROM
                + String.join(SqlUtils.SQL_COMMA_SEPARATOR, updateImageParams.getTables())
                + SqlUtils.WHERE
                + updateImageParams.getWhereSql();
        try {
            return queryRunner.query(beforeSql,
                    new UpdateSqlPreDataHandler(updateImageParams.getPrimaryKeys(), updateImageParams.getColumns()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ModifiedRecord> deleteSqlPreviousData(DeleteImageParams deleteImageParams) {
        StringBuilder beforeSql = new StringBuilder(SqlUtils.SELECT);
        beforeSql.append(String.join(SqlUtils.SQL_COMMA_SEPARATOR, deleteImageParams.getColumns()))
                .append(SqlUtils.FROM)
                .append(String.join(SqlUtils.SQL_COMMA_SEPARATOR, deleteImageParams.getTables()))
                .append(SqlUtils.WHERE)
                .append(deleteImageParams.getSqlWhere());

        try {
            return queryRunner.query(beforeSql.toString(),
                    new UpdateSqlPreDataHandler(
                            deleteImageParams.getPrimaryKeys(),
                            deleteImageParams.getColumns()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ModifiedRecord> selectSqlPreviousPrimaryKeys(SelectImageParams selectImageParams) {
        try {
            return queryRunner.query(selectImageParams.getSql(),
                    new UpdateSqlPreDataHandler(
                            selectImageParams.getPrimaryKeys(),
                            selectImageParams.getPrimaryKeys()));
        } catch (SQLException e) {
            log.error("txc > sql executor > query select sql image error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void tryLock(LockInfo lockInfo) throws SQLException {
        String lockSql = "INSERT INTO `" + txcSettingFactory.lockTableName() + "` (table_name, key_value, group_id, unit_id, x_lock, s_lock) values(?, ?, ?, ?, ?, ?)";
        queryRunner.insert(lockSql, new ScalarHandler<Integer>(),
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
        String undoLogSql = "SELECT * FROM `" + txcSettingFactory.undoLogTableName() + "` WHERE `group_id`=? and `unit_id`=?";
        Connection connection = null;
        try {
            BeanProcessor bean = new GenerousBeanProcessor();
            RowProcessor processor = new BasicRowProcessor(bean);
            UndoLogDO undoLogDo = queryRunner.query(undoLogSql, new BeanHandler<>(UndoLogDO.class, processor), groupId, unitId);
            txLogger.trace(groupId, unitId, "txc", "undoLogDo sql " + undoLogDo);
            if (Objects.isNull(undoLogDo)) {
                log.warn("txc . undo log not found!");
                return;
            }
            RollbackInfo rollbackInfo = SqlUtils.blobToObject(undoLogDo.getRollbackInfo(), RollbackInfo.class);
            txLogger.trace(groupId, unitId, "txc", "rollbackInfo sql " + rollbackInfo.toString());
            connection = queryRunner.getDataSource().getConnection();
            connection.setAutoCommit(false);
            for (StatementInfo statementInfo : rollbackInfo.getRollbackSqlList()) {
                log.info("txc > Apply undo log. sql: {}, params: {}", statementInfo.getSql(), statementInfo.getParams());
                queryRunner.update(connection, statementInfo.getSql(), statementInfo.getParams());
            }
            connection.commit();
        } catch (SQLException e) {
            log.error("txc > apply undo log error", e);
            try {
                DbUtils.rollback(connection);
            } catch (SQLException ignored) {
            }
            throw e;
        } finally {
            try {
                DbUtils.close(connection);
            } catch (SQLException ignored) {
            }
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
