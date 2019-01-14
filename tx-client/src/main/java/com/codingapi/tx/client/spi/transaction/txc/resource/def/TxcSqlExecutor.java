package com.codingapi.tx.client.spi.transaction.txc.resource.def;

import com.codingapi.tx.client.spi.transaction.txc.resource.def.bean.*;

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
     * @param updateImageParams
     * @return
     * @throws SQLException
     */
    List<ModifiedRecord> updateSqlPreviousData(Connection connection, UpdateImageParams updateImageParams) throws SQLException;

    /**
     * {@code delete} sql受影响数据
     *
     * @param deleteImageParams
     * @return
     * @throws SQLException
     */
    List<ModifiedRecord> deleteSqlPreviousData(Connection connection, DeleteImageParams deleteImageParams) throws SQLException;

    /**
     * {@code select} 语句受影响数据查询
     *
     * @param selectImageParams
     * @return
     * @throws SQLException
     */
    List<ModifiedRecord> selectSqlPreviousPrimaryKeys(Connection connection, SelectImageParams selectImageParams) throws SQLException;

    /**
     * 尝试写入锁信息到数据库
     *
     * @param lockInfo
     * @throws SQLException 获取锁失败时抛出
     */
    void tryLock(Connection connection, LockInfo lockInfo) throws SQLException;

    /**
     * 清除锁信息
     *
     * @param groupId
     * @param unitId  事务单元
     * @throws SQLException
     */
    void clearLock(String groupId, String unitId) throws SQLException;

    /**
     * 写undo_log
     *
     * @param undoLogDo
     */
    void writeUndoLog(UndoLogDO undoLogDo) throws SQLException;

    /**
     * 回滚undo_log
     *
     * @param groupId
     * @param unitId  事务单元
     * @throws SQLException
     */
    void applyUndoLog(String groupId, String unitId) throws SQLException;

    /**
     * 清除undo_log
     *
     * @param groupId
     * @param unitId  事务单元
     * @throws SQLException
     */
    void clearUndoLog(String groupId, String unitId) throws SQLException;
}
