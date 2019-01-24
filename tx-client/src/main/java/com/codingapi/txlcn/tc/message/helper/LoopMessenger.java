package com.codingapi.txlcn.tc.message.helper;

import com.codingapi.txlcn.commons.exception.LcnBusinessException;
import com.codingapi.txlcn.spi.message.MessageConstants;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.spi.message.params.JoinGroupParams;
import com.codingapi.txlcn.spi.message.params.NotifyGroupParams;
import com.codingapi.txlcn.spi.message.util.MessageUtils;
import com.codingapi.txlcn.tc.message.ReliableMessenger;
import com.codingapi.txlcn.tc.message.TMSearcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Description:
 * Date: 19-1-22 下午5:12
 *
 * @author ujued
 */
@Component
@Slf4j
public class LoopMessenger implements ReliableMessenger {

    private final RpcClient rpcClient;

    @Autowired
    public LoopMessenger(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public boolean acquireLocks(String groupId, Set<String> lockIdSet, int type) throws RpcException {
        MessageDto messageDto = request(MessageCreator.acquireLocks(groupId, lockIdSet, type), "release lock fail");
        return MessageUtils.statusOk(messageDto);
    }

    @Override
    public void releaseLocks(Set<String> lockIdList) throws RpcException {
        MessageDto messageDto = rpcClient.request(rpcClient.loadRemoteKey(), MessageCreator.releaseLocks(lockIdList));
        if (!MessageUtils.statusOk(messageDto)) {
            throw new RpcException("release locks fail.");
        }
    }

    @Override
    public void notifyGroup(String groupId, int transactionState) throws RpcException, LcnBusinessException {
        NotifyGroupParams notifyGroupParams = new NotifyGroupParams();
        notifyGroupParams.setGroupId(groupId);
        notifyGroupParams.setState(transactionState);
        MessageDto messageDto = request(MessageCreator.notifyGroup(notifyGroupParams));
        // 成功清理发起方事务
        if (!MessageUtils.statusOk(messageDto)) {
            throw new LcnBusinessException(messageDto.loadBean(Throwable.class));
        }
    }

    @Override
    public void joinGroup(String groupId, String unitId, String unitType, int transactionState) throws RpcException, LcnBusinessException {
        JoinGroupParams joinGroupParams = new JoinGroupParams();
        joinGroupParams.setGroupId(groupId);
        joinGroupParams.setUnitId(unitId);
        joinGroupParams.setUnitType(unitType);
        joinGroupParams.setTransactionState(transactionState);
        MessageDto messageDto = request(MessageCreator.joinGroup(joinGroupParams));
        if (!MessageUtils.statusOk(messageDto)) {
            throw new LcnBusinessException(messageDto.loadBean(Throwable.class));
        }
    }

    @Override
    public void createGroup(String groupId) throws RpcException, LcnBusinessException {
        // TxManager创建事务组
        MessageDto messageDto = request(MessageCreator.createGroup(groupId));
        if (!MessageUtils.statusOk(messageDto)) {
            throw new LcnBusinessException(messageDto.loadBean(Throwable.class));
        }
    }

    @Override
    public void reportInvalidTM(HashSet<String> invalidTMSet) throws RpcException {
        MessageDto messageDto = new MessageDto();
        messageDto.setAction(MessageConstants.ACTION_CLEAN_INVALID_TM);
        messageDto.setData(invalidTMSet);
        messageDto = request(messageDto);
        if (!MessageUtils.statusOk(messageDto)) {
            throw new RpcException(messageDto.loadBean(Throwable.class));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public HashSet<String> queryTMCluster() throws RpcException {
        MessageDto messageDto = new MessageDto();
        messageDto.setAction(MessageConstants.ACTION_REMAIN_TM);
        messageDto = request(messageDto);
        if (MessageUtils.statusOk(messageDto)) {
            return messageDto.loadBean(HashSet.class);
        }
        throw new RpcException(messageDto.loadBean(Throwable.class));
    }

    @Override
    public MessageDto request(MessageDto messageDto) throws RpcException {
        return request(messageDto, "request fail");
    }

    @Override
    public int clusterSize() {
        return rpcClient.loadAllRemoteKey().size();
    }


    /**
     * 强通讯
     *
     * @param messageDto            通讯数据
     * @param whenNonManagerMessage 异常提示
     * @return MessageDto
     * @throws RpcException RpcException
     */
    private MessageDto request(MessageDto messageDto, String whenNonManagerMessage) throws RpcException {
        while (true) {
            try {
                String remoteKey = rpcClient.loadRemoteKey();
                MessageDto result = rpcClient.request(remoteKey, messageDto);
                log.debug("request action: {}. TM[{}]", messageDto.getAction(), remoteKey);
                return result;
            } catch (RpcException e) {
                if (e.getCode() == RpcException.NON_TX_MANAGER) {
                    TMSearcher.search();
                    throw new RpcException(e.getCode(), whenNonManagerMessage + ". non tx-manager is alive.");
                }
            }
        }
    }
}
