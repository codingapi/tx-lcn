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
package com.codingapi.txlcn.client.core.txc.resource.def;

import com.codingapi.txlcn.client.core.txc.resource.def.bean.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Description: Txc模式相关数据操作
 * Date: 2018/12/13
 *
 * @author ujued
 */
public interface TxcSqlExecutor {

    /**
     * 创建锁表
     */
    void createLockTable();

    /**
     * 创建撤销信息表
     */
    void createUndoLogTable();

    /**
     * update sql 执行前受影响数据
     *
     * @param connection Connection
     * @param updateImageParams updateImageParams
     * @return list
     * @throws SQLException SQLException
     */
    List<ModifiedRecord> updateSqlPreviousData(Connection connection, UpdateImageParams updateImageParams) throws SQLException;

    /**
     * {@code delete} sql受影响数据
     * @param connection Connection
     * @param deleteImageParams deleteImageParams
     * @return list
     * @throws SQLException SQLException
     */
    List<ModifiedRecord> deleteSqlPreviousData(Connection connection, DeleteImageParams deleteImageParams) throws SQLException;

    /**
     * {@code select} 语句受影响数据查询
     *
     * @param connection Connection
     * @param selectImageParams selectImageParams
     * @return list
     * @throws SQLException SQLException
     */
    List<ModifiedRecord> selectSqlPreviousPrimaryKeys(Connection connection, SelectImageParams selectImageParams) throws SQLException;

    /**
     * 尝试写入锁信息到数据库
     *
     * @param connection Connection
     * @param lockInfo lockInfo
     * @throws SQLException 获取锁失败时抛出
     */
    void tryLock(Connection connection, LockInfo lockInfo) throws SQLException;

    /**
     * 清除锁信息
     *
     * @param groupId groupId
     * @param unitId  事务单元
     * @throws SQLException  SQLException
     */
    void clearLock(String groupId, String unitId) throws SQLException;

    /**
     * 写undo_log
     *
     * @param undoLogDo undoLogDo
     * @throws SQLException SQLException
     */
    void writeUndoLog(UndoLogDO undoLogDo) throws SQLException;

    /**
     * 回滚undo_log
     *
     * @param groupId groupId
     * @param unitId  事务单元
     * @throws SQLException SQLException
     */
    void applyUndoLog(String groupId, String unitId) throws SQLException;

    /**
     * 清除undo_log
     *
     * @param groupId groupId
     * @param unitId  事务单元
     * @throws SQLException SQLException
     */
    void clearUndoLog(String groupId, String unitId) throws SQLException;


    void undoRollbackInfoSql(Connection connection, RollbackInfo rollbackInfo) throws SQLException;
}
