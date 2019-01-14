package com.codingapi.tx.client.spi.transaction.txc.resource.def;

import com.codingapi.tx.client.spi.transaction.txc.resource.def.bean.*;
import com.codingapi.tx.commons.exception.TxClientException;
import com.codingapi.tx.commons.exception.TxcLogicException;

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
     * @throws TxcLogicException
     */
    void lockResource(LockInfo lockInfo, RollbackInfo rollbackInfo) throws TxcLogicException;

    /**
     * 锁定资源。{@code select} 被调用
     *
     * @param selectImageParams
     * @param isxLock
     * @throws TxcLogicException
     */
    void lockSelect(SelectImageParams selectImageParams, boolean isxLock) throws TxcLogicException;

    /**
     * {@code update} 前置镜像
     *
     * @param updateImageParams
     * @throws TxcLogicException
     */
    void resolveUpdateImage(UpdateImageParams updateImageParams) throws TxcLogicException;

    /**
     * {@code delete} 前置镜像
     *
     * @param deleteImageParams
     * @throws TxcLogicException
     */
    void resolveDeleteImage(DeleteImageParams deleteImageParams) throws TxcLogicException;

    /**
     * 写undo_log
     *
     * @param groupId
     * @param unitId
     * @param rollbackInfo
     * @throws TxcLogicException
     */
    void writeUndoLog(String groupId, String unitId, RollbackInfo rollbackInfo) throws TxcLogicException;

    /**
     * 清理TXC事务
     *
     * @param groupId
     * @param unitId
     * @throws TxcLogicException
     */
    void cleanTxc(String groupId, String unitId) throws TxcLogicException;

    /**
     * 撤销某事务单元数据库操作
     *
     * @param groupId
     * @param unitId
     * @throws TxcLogicException
     */
    void undo(String groupId, String unitId) throws TxcLogicException;
}
