package com.codingapi.txlcn.tm.cluster;

import com.codingapi.txlcn.spi.message.listener.ClientInitCallBack;
import com.codingapi.txlcn.spi.message.TMCluster;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 19-1-24 下午3:24
 *
 * @author ujued
 */
@Component
@Slf4j
public class ClusterRpcInitCallback implements ClientInitCallBack {

    private final ClusterMessenger clusterMessenger;

    private final TMCluster tmCluster;

    @Autowired
    public ClusterRpcInitCallback(ClusterMessenger clusterMessenger, TMCluster tmCluster) {
        this.clusterMessenger = clusterMessenger;
        this.tmCluster = tmCluster;
    }

    @Override
    public void connected(String remoteKey, int clusterSize) {
        new Thread(() -> {
            try {
                clusterMessenger.queryTMInfoPacket(remoteKey);
            } catch (RpcException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void disconnected(String remoteKey) {
        tmCluster.cleanCluster(remoteKey);
    }
}
