package com.codingapi.tx.client.spi.transaction.txc.resource.sql.def;

import com.codingapi.tx.client.spi.transaction.txc.resource.sql.def.bean.*;

import java.sql.SQLException;

/**
 * Description: TXC事务模式植入的业务
 * Date: 2018/12/17
 *
 * @author ujued
 */
public interface TxcService {
    /**
     * 锁定资源。{@code update} {@code delete} 被调用
     *
     * @param lockInfo
     * @param rollbackInfo
     * @throws SQLException
     */
    void lockResource(LockInfo lockInfo, RollbackInfo rollbackInfo) throws SQLException;

    /**
     * 锁定资源。{@code select} 被调用
     *
     * @param selectImageParams
     * @param isxLock
     * @throws SQLException
     */
    void lockSelect(SelectImageParams selectImageParams, boolean isxLock) throws SQLException;

    /**
     * {@code update} 前置镜像
     *
     * @param updateImageParams
     * @throws SQLException
     */
    void resolveUpdateImage(UpdateImageParams updateImageParams) throws SQLException;

    /**
     * {@code delete} 前置镜像
     *
     * @param deleteImageParams
     * @throws SQLException
     */
    void resolveDeleteImage(DeleteImageParams deleteImageParams) throws SQLException;

    /**
     * 写undo_log
     *
     * @param groupId
     * @param unitId
     * @param rollbackInfo
     */
    void writeUndoLog(String groupId, String unitId, RollbackInfo rollbackInfo);

    /**
     * 清理TXC事务
     *
     * @param groupId
     * @param unitId
     * @throws SQLException
     */
    void cleanTxc(String groupId, String unitId) throws SQLException;

    /**
     * 撤销某事务单元数据库操作
     *
     * @param groupId
     * @param unitId
     * @throws SQLException
     */
    void undo(String groupId, String unitId) throws SQLException;
}
