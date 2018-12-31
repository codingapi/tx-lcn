package com.codingapi.tx.client.spi.transaction.txc.resource.sql;

import com.codingapi.tx.client.bean.TxTransactionLocal;
import com.codingapi.tx.client.spi.transaction.txc.resource.sql.def.TxcService;
import com.codingapi.tx.client.spi.transaction.txc.resource.sql.def.TxcSqlExecutor;
import com.codingapi.tx.client.spi.transaction.txc.resource.sql.def.bean.*;
import com.codingapi.tx.client.spi.transaction.txc.resource.sql.util.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Date: 2018/12/17
 *
 * @author ujued
 */
@Service
@Slf4j
public class TxcServiceImpl implements TxcService {

    @Autowired
    private TxcSqlExecutor txcSqlExecutor;

    @Override
    public void lockResource(LockInfo lockInfo, RollbackInfo rollbackInfo) throws SQLException {
        try {
            TxTransactionLocal.makeUnProxy();
            // key value MD5 HEX to store
            lockInfo.setKeyValue(DigestUtils.md5DigestAsHex(lockInfo.getKeyValue().getBytes(StandardCharsets.UTF_8)));
            txcSqlExecutor.tryLock(lockInfo);
        } catch (SQLException e) {
            rollbackInfo.setStatus(-1);
            throw new SQLException("Resource is locked! Place try again later.");
        } finally {
            TxTransactionLocal.undoProxyStatus();
        }
    }

    @Override
    public void lockSelect(SelectImageParams selectImageParams, boolean isxLock) throws SQLException {
        List<ModifiedRecord> modifiedRecords;
        try {
            TxTransactionLocal.makeUnProxy();
            modifiedRecords = txcSqlExecutor.selectSqlPreviousPrimaryKeys(selectImageParams);
        } finally {
            TxTransactionLocal.undoProxyStatus();
        }
        for (ModifiedRecord modifiedRecord : modifiedRecords) {
            for (Map.Entry<String, FieldCluster> entry : modifiedRecord.getFieldClusters().entrySet()) {
                String k = entry.getKey();
                FieldCluster v = entry.getValue();
                lockResource(new LockInfo()
                        .setGroupId(selectImageParams.getGroupId())
                        .setUnitId(selectImageParams.getUnitId())
                        .setxLock(isxLock)
                        .setKeyValue(v.getPrimaryKeys().toString())
                        .setTableName(k), selectImageParams.getRollbackInfo());
            }
        }
    }

    @Override
    public void resolveUpdateImage(UpdateImageParams updateImageParams) throws SQLException {

        // 前置镜像数据集
        List<ModifiedRecord> modifiedRecords;
        try {
            TxTransactionLocal.makeUnProxy();
            modifiedRecords = txcSqlExecutor.updateSqlPreviousData(updateImageParams);
        } finally {
            TxTransactionLocal.undoProxyStatus();
        }


        // Build reverse sql
        for (ModifiedRecord modifiedRecord : modifiedRecords) {
            for (Map.Entry<String, FieldCluster> entry : modifiedRecord.getFieldClusters().entrySet()) {
                String k = entry.getKey();
                FieldCluster v = entry.getValue();

                Object[] params = new Object[v.getFields().size() + v.getPrimaryKeys().size()];

                StringBuilder rollbackSql = new StringBuilder()
                        .append(SqlUtils.UPDATE)
                        .append(k)
                        .append(SqlUtils.SET);
                int index = 0;
                for (int i = 0; i < v.getFields().size(); i++, index++) {
                    FieldValue fieldValue = v.getFields().get(i);
                    rollbackSql.append(fieldValue.getFieldName())
                            .append("=?")
                            .append(SqlUtils.SQL_COMMA_SEPARATOR);
                    params[index] = fieldValue.getValue();
                }
                SqlUtils.cutSuffix(SqlUtils.SQL_COMMA_SEPARATOR, rollbackSql);
                rollbackSql.append(SqlUtils.WHERE);

                for (int i = 0; i < v.getPrimaryKeys().size(); i++, index++) {
                    FieldValue fieldValue = v.getPrimaryKeys().get(i);
                    rollbackSql.append(fieldValue.getFieldName())
                            .append("=?")
                            .append(SqlUtils.AND);
                    params[index] = fieldValue.getValue();
                }
                SqlUtils.cutSuffix(SqlUtils.AND, rollbackSql);
                updateImageParams.getRollbackInfo().getRollbackSqlList().add(new StatementInfo(rollbackSql.toString(), params));

                // Lock Resource
                this.lockResource(new LockInfo()
                        .setxLock(true)
                        .setGroupId(updateImageParams.getGroupId())
                        .setUnitId(updateImageParams.getUnitId())
                        .setKeyValue(v.getPrimaryKeys().toString())
                        .setTableName(k), updateImageParams.getRollbackInfo());
            }
        }
        log.debug("rollback info: {}", updateImageParams.getRollbackInfo());
    }

    @Override
    public void resolveDeleteImage(DeleteImageParams deleteImageParams) throws SQLException {

        // 前置数据
        List<ModifiedRecord> modifiedRecords;
        try {
            TxTransactionLocal.makeUnProxy();
            modifiedRecords = txcSqlExecutor.deleteSqlPreviousData(deleteImageParams);
        } finally {
            TxTransactionLocal.undoProxyStatus();
        }

        // rollback sql
        for (ModifiedRecord modifiedRecord : modifiedRecords) {
            for (Map.Entry<String, FieldCluster> entry : modifiedRecord.getFieldClusters().entrySet()) {
                String k = entry.getKey();
                FieldCluster v = entry.getValue();

                StringBuilder rollbackSql = new StringBuilder(SqlUtils.INSERT).append(k).append('(');
                StringBuilder values = new StringBuilder();
                Object[] params = new Object[v.getFields().size()];
                for (int i = 0; i < v.getFields().size(); i++) {
                    FieldValue fieldValue = v.getFields().get(i);
                    rollbackSql.append('`').append(fieldValue.getFieldName()).append('`').append(SqlUtils.SQL_COMMA_SEPARATOR);
                    values.append("?, ");
                    params[i] = fieldValue.getValue();
                }
                SqlUtils.cutSuffix(SqlUtils.SQL_COMMA_SEPARATOR, rollbackSql);
                SqlUtils.cutSuffix(SqlUtils.SQL_COMMA_SEPARATOR, values);
                rollbackSql.append(") values(").append(values).append(')');
                deleteImageParams.getRollbackInfo().getRollbackSqlList().add(new StatementInfo(rollbackSql.toString(), params));

                // Lock Resource
                this.lockResource(new LockInfo()
                        .setxLock(true)
                        .setGroupId(deleteImageParams.getGroupId())
                        .setUnitId(deleteImageParams.getUnitId())
                        .setKeyValue(v.getPrimaryKeys().toString())
                        .setTableName(k), deleteImageParams.getRollbackInfo());
            }
        }
    }

    @Override
    public void writeUndoLog(String groupId, String unitId, RollbackInfo rollbackInfo) {
        if (rollbackInfo.getRollbackSqlList().size() == 0) {
            return;
        }
        UndoLogDO undoLogDO = new UndoLogDO();
        undoLogDO.setGroupId(groupId);
        undoLogDO.setUnitId(unitId);
        undoLogDO.setRollbackInfo(SqlUtils.objectToBlob(rollbackInfo));

        // 表存在
        try {
            TxTransactionLocal.makeUnProxy();
            txcSqlExecutor.writeUndoLog(undoLogDO);
        } catch (SQLException e) {
            log.error("error: {} code: {}", e.getMessage(), e.getErrorCode());
        } finally {
            TxTransactionLocal.undoProxyStatus();
        }
    }

    @Override
    public void cleanTxc(String groupId, String unitId) throws SQLException {
        // 清理事务单元相关锁
        try {
            TxTransactionLocal.makeUnProxy();
            txcSqlExecutor.clearLock(groupId, unitId);
        } catch (SQLException e) {
            if (e.getErrorCode() != SqlUtils.MYSQL_TABLE_NOT_EXISTS_CODE) {
                throw e;
            }
        } finally {
            TxTransactionLocal.undoProxyStatus();
        }

        // 清理事务单元相关undo_log
        try {
            TxTransactionLocal.makeUnProxy();
            txcSqlExecutor.clearUndoLog(groupId, unitId);
        } catch (SQLException e) {
            if (e.getErrorCode() != SqlUtils.MYSQL_TABLE_NOT_EXISTS_CODE) {
                throw e;
            }
        } finally {
            TxTransactionLocal.undoProxyStatus();
        }
    }

    @Override
    public void undo(String groupId, String unitId) throws SQLException {
        try {
            TxTransactionLocal.makeUnProxy();
            txcSqlExecutor.applyUndoLog(groupId, unitId);
        } finally {
            TxTransactionLocal.undoProxyStatus();
        }
    }
}
