package com.codingapi.txlcn.tc.message;

import com.codingapi.txlcn.commons.exception.LcnBusinessException;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.exception.RpcException;

import java.util.Set;

/**
 * Description:
 * Date: 19-1-22 下午5:14
 *
 * @author ujued
 */
public interface ReliableMessenger {
    boolean acquireLocks(String groupId, Set<String> lockIdSet, int type) throws RpcException;

    void releaseLocks(Set<String> lockIdList) throws RpcException;

    void notifyGroup(String groupId, int transactionState) throws RpcException, LcnBusinessException;

    void joinGroup(String groupId, String unitId, String unitType, int transactionState) throws RpcException, LcnBusinessException;

    void createGroup(String groupId) throws RpcException, LcnBusinessException;

    MessageDto request(MessageDto messageDto) throws RpcException;
}
