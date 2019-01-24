package com.codingapi.txlcn.tm.cluster;

import com.codingapi.txlcn.commons.exception.FastStorageException;
import com.codingapi.txlcn.commons.util.ApplicationInformation;
import com.codingapi.txlcn.spi.message.MessageConstants;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.TMCluster;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.spi.message.params.InitClientParams;
import com.codingapi.txlcn.spi.message.params.NotifyConnectParams;
import com.codingapi.txlcn.spi.message.util.MessageUtils;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Description:
 * Date: 19-1-24 下午4:40
 *
 * @author ujued
 */
@Component
public class DefaultClusterMessenger implements ClusterMessenger {

    private final RpcClient rpcClient;

    private final TMCluster tmCluster;

    private final String tmId;

    private final FastStorage fastStorage;

    @Autowired
    public DefaultClusterMessenger(RpcClient rpcClient, TMCluster tmCluster,
                                   ConfigurableEnvironment environment, ServerProperties serverProperties, FastStorage fastStorage) {
        this.rpcClient = rpcClient;
        this.tmCluster = tmCluster;
        this.tmId = ApplicationInformation.modId(environment, serverProperties);
        this.fastStorage = fastStorage;
    }

    @Override
    public void queryTMInfoPacket(String tmKey) throws RpcException {
        InitClientParams initClientParams = new InitClientParams();
        initClientParams.setAppName(tmId);
        MessageDto messageDto = new MessageDto();
        messageDto.setData(initClientParams);
        messageDto.setGroupId(MessageConstants.ACTION_INIT_GROUPID);
        messageDto.setAction(MessageConstants.ACTION_INIT_CLIENT);

        MessageDto msg = rpcClient.request(tmKey, messageDto);

        if (msg.getData() != null) {
            InitClientParams resParams = msg.loadBean(InitClientParams.class);
            tmCluster.toCluster(resParams.getAppName(), tmKey);
        }
    }

    @Override
    public void refreshTMCluster(String tmKey, NotifyConnectParams notifyConnectParams) throws RpcException {
        MessageDto messageDto = new MessageDto();
        messageDto.setData(notifyConnectParams);
        messageDto.setAction(MessageConstants.ACTION_AUTO_CLUSTER);
        MessageDto res = rpcClient.request(tmKey, messageDto);
        if (!MessageUtils.statusOk(res)) {
            throw new RpcException();
        }
    }

    @Override
    public String tmRpcKeyByModId(String modId) throws RpcException {
        try {
            List<String> tmList = fastStorage.findTMsByModId(modId);
            if (tmList.isEmpty()) {
                throw new RpcException();
            }
            return Optional.ofNullable(tmCluster.rpcKey(tmList.get(0))).orElseThrow(RpcException::new);
        } catch (FastStorageException e) {
            throw new RpcException(e);
        }
    }
}
