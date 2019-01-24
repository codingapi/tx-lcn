package com.codingapi.txlcn.tm.cluster;

import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
import com.codingapi.txlcn.spi.message.RpcClientInitializer;
import com.codingapi.txlcn.spi.message.TMCluster;
import com.codingapi.txlcn.spi.message.dto.TxManagerHost;
import com.codingapi.txlcn.spi.message.params.NotifyConnectParams;
import com.codingapi.txlcn.tm.config.TxManagerConfig;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * Date: 19-1-24 下午3:32
 *
 * @author ujued
 */
@Component
public class ClusterRpcClientInitializer implements TxLcnInitializer {

    private final RpcClientInitializer rpcClientInitializer;

    private final FastStorage fastStorage;

    private final TMCluster tmCluster;

    private final ClusterMessenger clusterMessenger;

    private final TxManagerConfig txManagerConfig;

    @Autowired
    public ClusterRpcClientInitializer(RpcClientInitializer rpcClientInitializer, FastStorage fastStorage, TMCluster tmCluster, ClusterMessenger clusterMessenger, TxManagerConfig txManagerConfig) {
        this.rpcClientInitializer = rpcClientInitializer;
        this.fastStorage = fastStorage;
        this.tmCluster = tmCluster;
        this.clusterMessenger = clusterMessenger;
        this.txManagerConfig = txManagerConfig;
    }

    @Override
    public void init() throws Exception {
        // 1. RPC Client for TM Cluster
        List<String> tmAddresses = fastStorage.findTMAddresses();
        rpcClientInitializer.init(TxManagerHost.parserList(tmAddresses));


        // 2. Auto cluster
        NotifyConnectParams notifyConnectParams = new NotifyConnectParams();
        notifyConnectParams.setHost(txManagerConfig.getHost());
        notifyConnectParams.setPort(txManagerConfig.getPort());

        List<String> addressList = tmCluster.tmKeys();

        for (String address : addressList) {
            clusterMessenger.refreshTMCluster(address, notifyConnectParams);
        }
    }
}
