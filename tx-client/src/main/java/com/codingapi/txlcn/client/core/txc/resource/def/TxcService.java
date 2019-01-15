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

import com.codingapi.txlcn.commons.exception.TxcLogicException;
import com.codingapi.txlcn.client.core.txc.resource.def.bean.*;

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
     * @param lockInfo lockInfo
     * @param rollbackInfo rollbackInfo
     * @throws TxcLogicException TxcLogicException
     */
    void lockResource(LockInfo lockInfo, RollbackInfo rollbackInfo) throws TxcLogicException;

    /**
     * 锁定资源。{@code select} 被调用
     *
     * @param selectImageParams selectImageParams
     * @param isxLock isxLock
     * @throws TxcLogicException TxcLogicException
     */
    void lockSelect(SelectImageParams selectImageParams, boolean isxLock) throws TxcLogicException;

    /**
     * {@code update} 前置镜像
     *
     * @param updateImageParams updateImageParams
     * @throws TxcLogicException TxcLogicException
     */
    void resolveUpdateImage(UpdateImageParams updateImageParams) throws TxcLogicException;

    /**
     * {@code delete} 前置镜像
     *
     * @param deleteImageParams deleteImageParams
     * @throws TxcLogicException TxcLogicException
     */
    void resolveDeleteImage(DeleteImageParams deleteImageParams) throws TxcLogicException;

    /**
     * 写undo_log
     *
     * @param groupId groupId
     * @param unitId unitId
     * @param rollbackInfo rollbackInfo
     * @throws TxcLogicException TxcLogicException
     */
    void writeUndoLog(String groupId, String unitId, RollbackInfo rollbackInfo) throws TxcLogicException;

    /**
     * 清理TXC事务
     *
     * @param groupId groupId
     * @param unitId unitId
     * @throws TxcLogicException TxcLogicException
     */
    void cleanTxc(String groupId, String unitId) throws TxcLogicException;

    /**
     * 撤销某事务单元数据库操作
     *
     * @param groupId groupId
     * @param unitId unitId
     * @throws TxcLogicException TxcLogicException
     */
    void undo(String groupId, String unitId) throws TxcLogicException;
}
