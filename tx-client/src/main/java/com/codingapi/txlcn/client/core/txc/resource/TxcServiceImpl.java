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

import com.codingapi.txlcn.client.bean.DTXLocal;
import com.codingapi.txlcn.client.core.txc.resource.def.TxcService;
import com.codingapi.txlcn.client.core.txc.resource.def.TxcSqlExecutor;
import com.codingapi.txlcn.client.core.txc.resource.init.TxcExceptionConnectionPool;
import com.codingapi.txlcn.client.core.txc.resource.util.SqlUtils;
import com.codingapi.txlcn.commons.exception.TxcLogicException;
import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.client.core.txc.resource.def.bean.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/17
 *
 * @author ujued
 */
@Service
@Slf4j
public class TxcServiceImpl implements TxcService {

    private final TxcSqlExecutor txcSqlExecutor;

    private final TxcExceptionConnectionPool txcExceptionConnectionPool;

    private final TxLogger txLogger;

    @Autowired
    public TxcServiceImpl(TxcSqlExecutor txcSqlExecutor, TxcExceptionConnectionPool txcExceptionConnectionPool, TxLogger txLogger) {
        this.txcSqlExecutor = txcSqlExecutor;
        this.txcExceptionConnectionPool = txcExceptionConnectionPool;
        this.txLogger = txLogger;
    }

    @Override
    public void lockResource(LockInfo lockInfo, RollbackInfo rollbackInfo) throws TxcLogicException {
        try {
            Connection connection = (Connection) DTXLocal.cur().getResource();
            // key value MD5 HEX to store
            lockInfo.setKeyValue(DigestUtils.md5DigestAsHex(lockInfo.getKeyValue().getBytes(StandardCharsets.UTF_8)));
            txcSqlExecutor.tryLock(connection, lockInfo);
        } catch (SQLException e) {
            rollbackInfo.setStatus(-1);
            throw new TxcLogicException("Resource is locked! Place try again later.");
        }
    }

    @Override
    public void lockSelect(SelectImageParams selectImageParams, boolean isxLock) throws TxcLogicException {
        Connection connection = (Connection) DTXLocal.cur().getResource();
        try {
            List<ModifiedRecord> modifiedRecords = txcSqlExecutor.selectSqlPreviousPrimaryKeys(connection, selectImageParams);
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
        } catch (SQLException e) {
            throw new TxcLogicException(e);
        }
    }

    @Override
    public void resolveUpdateImage(UpdateImageParams updateImageParams) throws TxcLogicException {

        // 前置镜像数据集
        List<ModifiedRecord> modifiedRecords;
        Connection connection = (Connection) DTXLocal.cur().getResource();
        try {
            modifiedRecords = txcSqlExecutor.updateSqlPreviousData(connection, updateImageParams);
        } catch (SQLException e) {
            throw new TxcLogicException(e);
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
                        .setKeyValue(v.getPrimaryKeys().toString())
                        .setGroupId(updateImageParams.getGroupId())
                        .setUnitId(updateImageParams.getUnitId())
                        .setTableName(k), updateImageParams.getRollbackInfo());
            }
        }
        log.debug("rollback info: {}", updateImageParams.getRollbackInfo());
    }

    @Override
    public void resolveDeleteImage(DeleteImageParams deleteImageParams) throws TxcLogicException {

        // 前置数据
        List<ModifiedRecord> modifiedRecords;
        Connection connection = (Connection) DTXLocal.cur().getResource();
        try {
            modifiedRecords = txcSqlExecutor.deleteSqlPreviousData(connection, deleteImageParams);
        } catch (SQLException e) {
            throw new TxcLogicException(e);
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
    public void writeUndoLog(String groupId, String unitId, RollbackInfo rollbackInfo) throws TxcLogicException {
        if (rollbackInfo.getRollbackSqlList().size() == 0) {
            return;
        }
        UndoLogDO undoLogDO = new UndoLogDO();
        undoLogDO.setGroupId(groupId);
        undoLogDO.setUnitId(unitId);
        undoLogDO.setRollbackInfo(SqlUtils.objectToBlob(rollbackInfo));

        // 表存在
        try {
            DTXLocal.makeUnProxy();
            txcSqlExecutor.writeUndoLog(undoLogDO);
        } catch (SQLException e) {
            throw new TxcLogicException(e);
        } finally {
            DTXLocal.undoProxyStatus();
        }
    }

    @Override
    public void cleanTxc(String groupId, String unitId) throws TxcLogicException {
        // 清理事务单元相关锁
        try {
            DTXLocal.makeUnProxy();
            txcSqlExecutor.clearLock(groupId, unitId);
        } catch (SQLException e) {
            if (e.getErrorCode() != SqlUtils.MYSQL_TABLE_NOT_EXISTS_CODE) {
                throw new TxcLogicException(e);
            }
        } finally {
            DTXLocal.undoProxyStatus();
        }

        // 清理事务单元相关undo_log
        try {
            DTXLocal.makeUnProxy();
            txcSqlExecutor.clearUndoLog(groupId, unitId);
        } catch (SQLException e) {
            if (e.getErrorCode() != SqlUtils.MYSQL_TABLE_NOT_EXISTS_CODE) {
                throw new TxcLogicException(e);
            }
        } finally {
            DTXLocal.undoProxyStatus();
        }
    }

    @Override
    public void undo(String groupId, String unitId) throws TxcLogicException {
        try {
            DTXLocal.makeUnProxy();
            txcSqlExecutor.applyUndoLog(groupId, unitId);
        } catch (SQLException e) {
            // 撤销失败 txcExceptionConnectionPool 作撤销
            if (Objects.nonNull(DTXLocal.cur())) {
                RollbackInfo rollbackInfo = (RollbackInfo) DTXLocal.cur().getAttachment();
                if (Objects.nonNull(rollbackInfo)) {
                    txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "rollback by txEx pool.");
                    Connection connection = null;
                    try {
                        connection = txcExceptionConnectionPool.getConnection();
                        txcSqlExecutor.undoRollbackInfoSql(connection, rollbackInfo);
                    } catch (SQLException e1) {
                        throw new TxcLogicException(e1);
                    } finally {
                        try {
                            DbUtils.close(connection);
                        } catch (SQLException ignored) {
                        }
                    }
                }
            }
        } finally {
            DTXLocal.undoProxyStatus();
        }
    }
}
