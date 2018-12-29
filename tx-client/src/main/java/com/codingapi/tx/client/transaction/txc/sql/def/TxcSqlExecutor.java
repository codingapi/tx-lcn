package com.codingapi.tx.client.transaction.txc.sql.def;

import com.codingapi.tx.client.transaction.txc.sql.def.bean.*;

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
     */
    List<ModifiedRecord> updateSqlPreviousData(UpdateImageParams updateImageParams);

    /**
     * {@code delete} sql受影响数据
     *
     * @param deleteImageParams
     * @return
     */
    List<ModifiedRecord> deleteSqlPreviousData(DeleteImageParams deleteImageParams);

    /**
     * {@code select} 语句受影响数据查询
     *
     * @param selectImageParams
     * @return
     */
    List<ModifiedRecord> selectSqlPreviousPrimaryKeys(SelectImageParams selectImageParams);

    /**
     * 尝试写入锁信息到数据库
     *
     * @param lockInfo
     * @throws SQLException 获取锁失败时抛出
     */
    void tryLock(LockInfo lockInfo) throws SQLException;

    /**
     * 清除锁信息
     *
     * @param groupId
     * @param unitId 事务单元
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
     * @param unitId 事务单元
     * @throws SQLException
     */
    void applyUndoLog(String groupId, String unitId) throws SQLException;

    /**
     * 清除undo_log
     *
     * @param groupId
     * @param unitId 事务单元
     * @throws SQLException
     */
    void clearUndoLog(String groupId, String unitId) throws SQLException;
}
