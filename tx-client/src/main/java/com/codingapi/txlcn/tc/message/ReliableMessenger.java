package com.codingapi.txlcn.tc.message;

import com.codingapi.txlcn.spi.message.exception.RpcException;

/**
 * Description:
 * Date: 19-1-22 下午5:14
 *
 * @author ujued
 */
public interface ReliableMessenger {

    boolean acquireLock(String groupId, String lockId, int type) throws RpcException;

    boolean releaseLock(String groupId, String lockId) throws RpcException;
}
