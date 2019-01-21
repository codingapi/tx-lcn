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
import com.codingapi.txlcn.client.core.txc.resource.def.bean.*;
import com.codingapi.txlcn.client.core.txc.resource.undo.TableRecord;
import com.codingapi.txlcn.client.core.txc.resource.undo.TableRecordList;
import com.codingapi.txlcn.client.core.txc.resource.undo.UndoLogAnalyser;
import com.codingapi.txlcn.client.core.txc.resource.util.SqlUtils;
import com.codingapi.txlcn.client.corelog.txc.TxcLogHelper;
import com.codingapi.txlcn.commons.exception.TxcLogicException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Date: 2018/12/17
 *
 * @author ujued
 */

@Slf4j
@Component
public class TxcServiceImpl implements TxcService {

    private final TxcSqlExecutor txcSqlExecutor;

    private final TxcLogHelper txcLogHelper;

    @Autowired
    public TxcServiceImpl(TxcSqlExecutor txcSqlExecutor, TxcLogHelper txcLogHelper) {
        this.txcSqlExecutor = txcSqlExecutor;
        this.txcLogHelper = txcLogHelper;
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


        TableRecordList tableRecords = new TableRecordList();

        // Build reverse sql
        for (ModifiedRecord modifiedRecord : modifiedRecords) {
            for (Map.Entry<String, FieldCluster> entry : modifiedRecord.getFieldClusters().entrySet()) {
                TableRecord tableRecord = new TableRecord();
                tableRecord.setTableName(entry.getKey());
                tableRecord.setFieldCluster(entry.getValue());
                tableRecords.getTableRecords().add(tableRecord);

                // Lock Resource
                this.lockResource(new LockInfo()
                        .setxLock(true)
                        .setKeyValue(tableRecord.getFieldCluster().getPrimaryKeys().toString())
                        .setGroupId(updateImageParams.getGroupId())
                        .setUnitId(updateImageParams.getUnitId())
                        .setTableName(tableRecord.getTableName()), updateImageParams.getRollbackInfo());
            }
        }

        // save to h2db
        UndoLogDO undoLogDO = new UndoLogDO();
        undoLogDO.setGroupId(updateImageParams.getGroupId());
        undoLogDO.setUnitId(updateImageParams.getUnitId());
        undoLogDO.setRollbackInfo(SqlUtils.objectToBlob(tableRecords));
        undoLogDO.setSqlType(SqlUtils.SQL_TYPE_UPDATE);
        try {
            txcLogHelper.saveUndoLog(undoLogDO);
        } catch (SQLException e) {
            throw new TxcLogicException(e);
        }
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

        TableRecordList tableRecords = new TableRecordList();

        // rollback sql
        for (ModifiedRecord modifiedRecord : modifiedRecords) {
            for (Map.Entry<String, FieldCluster> entry : modifiedRecord.getFieldClusters().entrySet()) {
                TableRecord tableRecord = new TableRecord();
                tableRecord.setTableName(entry.getKey());
                tableRecord.setFieldCluster(entry.getValue());
                tableRecords.getTableRecords().add(tableRecord);

                // Lock Resource
                this.lockResource(new LockInfo()
                        .setxLock(true)
                        .setGroupId(deleteImageParams.getGroupId())
                        .setUnitId(deleteImageParams.getUnitId())
                        .setKeyValue(tableRecord.getFieldCluster().getPrimaryKeys().toString())
                        .setTableName(tableRecord.getTableName()), deleteImageParams.getRollbackInfo());
            }
        }

        // save to db
        UndoLogDO undoLogDO = new UndoLogDO();
        undoLogDO.setRollbackInfo(SqlUtils.objectToBlob(tableRecords));
        undoLogDO.setGroupId(deleteImageParams.getGroupId());
        undoLogDO.setUnitId(deleteImageParams.getUnitId());
        undoLogDO.setSqlType(SqlUtils.SQL_TYPE_DELETE);
        try {
            txcLogHelper.saveUndoLog(undoLogDO);
        } catch (SQLException e) {
            throw new TxcLogicException(e);
        }
    }

    @Override
    public void resolveInsertImage(InsertImageParams insertImageParams) throws TxcLogicException {
        List<FieldValue> primaryKeys = new ArrayList<>();
        FieldCluster fieldCluster = new FieldCluster();
        fieldCluster.setPrimaryKeys(primaryKeys);
        try {
            for (int i = 0; i < insertImageParams.getPrimaryKeyValuesList().size(); i++) {
                insertImageParams.getResultSet().next();
                Map<String, Object> pks = insertImageParams.getPrimaryKeyValuesList().get(i);
                for (String key : insertImageParams.getFullyQualifiedPrimaryKeys()) {
                    FieldValue fieldValue = new FieldValue();
                    fieldValue.setFieldName(key);
                    if (pks.containsKey(key)) {
                        fieldValue.setValue(pks.get(key));
                    } else {
                        fieldValue.setValue(insertImageParams.getResultSet().getObject(1));
                    }
                    primaryKeys.add(fieldValue);
                }
            }
        } catch (SQLException e) {
            throw new TxcLogicException(e);
        } finally {
            try {
                DbUtils.close(insertImageParams.getResultSet());
            } catch (SQLException ignored) {
            }
        }

        // save to db
        TableRecordList tableRecords = new TableRecordList();
        tableRecords.getTableRecords().add(new TableRecord(insertImageParams.getTableName(), fieldCluster));

        UndoLogDO undoLogDO = new UndoLogDO();
        undoLogDO.setRollbackInfo(SqlUtils.objectToBlob(tableRecords));
        undoLogDO.setUnitId(insertImageParams.getUnitId());
        undoLogDO.setGroupId(insertImageParams.getGroupId());
        undoLogDO.setSqlType(SqlUtils.SQL_TYPE_INSERT);
        try {
            txcLogHelper.saveUndoLog(undoLogDO);
        } catch (SQLException e) {
            throw new TxcLogicException(e);
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
            txcLogHelper.deleteUndoLog(groupId, unitId);
        } catch (SQLException e) {
            log.error(" error {}-----------------{}", e.getErrorCode(), e.getMessage());
        }
    }

    @Override
    public void undo(String groupId, String unitId) throws TxcLogicException {
        try {
            List<StatementInfo> statementInfoList = new ArrayList<>();
            List<UndoLogDO> undoLogDOList = txcLogHelper.getUndoLogByGroupAndUnitId(groupId, unitId);

            for (UndoLogDO undoLogDO : undoLogDOList) {
                TableRecordList tableRecords = SqlUtils.blobToObject(undoLogDO.getRollbackInfo(), TableRecordList.class);
                switch (undoLogDO.getSqlType()) {
                    case SqlUtils.SQL_TYPE_UPDATE:
                        tableRecords.getTableRecords().forEach(tableRecord -> statementInfoList.add(UndoLogAnalyser.update(tableRecord)));
                        break;
                    case SqlUtils.SQL_TYPE_DELETE:
                        tableRecords.getTableRecords().forEach(tableRecord -> statementInfoList.add(UndoLogAnalyser.delete(tableRecord)));
                        break;
                    case SqlUtils.SQL_TYPE_INSERT:
                        tableRecords.getTableRecords().forEach(tableRecord -> statementInfoList.add(UndoLogAnalyser.insert(tableRecord)));
                        break;
                    default:
                        break;
                }
            }
            txcSqlExecutor.applyUndoLog(statementInfoList);
        } catch (SQLException e) {
            throw new TxcLogicException(e);
        }
    }
}
