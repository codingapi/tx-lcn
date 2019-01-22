package com.codingapi.txlcn.tc.message.helper;

import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.spi.message.util.MessageUtils;
import com.codingapi.txlcn.tc.message.ReliableMessenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 19-1-22 下午5:12
 *
 * @author ujued
 */
@Component
public class LoopMessenger implements ReliableMessenger {

    private final RpcClient rpcClient;

    @Autowired
    public LoopMessenger(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }


    @Override
    public boolean acquireLock(String groupId, String lockId, int type) throws RpcException {
        MessageDto messageDto = TxMangerReporter.requestUntilNonManager(rpcClient,
                MessageCreator.acquireLock(groupId, lockId, type), "acquire lock fail.");
        return MessageUtils.statusOk(messageDto);
    }

    @Override
    public boolean releaseLock(String groupId, String lockId) throws RpcException {
        MessageDto messageDto = TxMangerReporter.requestUntilNonManager(rpcClient,
                MessageCreator.releaseLock(groupId, lockId), "release lock fail.");
        return MessageUtils.statusOk(messageDto);
    }
}
