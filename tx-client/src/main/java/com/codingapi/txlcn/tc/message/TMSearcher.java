package com.codingapi.txlcn.tc.message;

import com.codingapi.txlcn.spi.message.RpcClientInitializer;
import com.codingapi.txlcn.spi.message.dto.TxManagerHost;
import com.codingapi.txlcn.tc.config.TxClientConfig;

import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Date: 19-1-23 下午5:54
 *
 * @author ujued
 */
public class TMSearcher {

    private static RpcClientInitializer RPC_CLIENT_INITIALIZER;

    private static List<TxManagerHost> tmCluster;

    public static void init(RpcClientInitializer rpcClientInitializer, TxClientConfig clientConfig) {
        RPC_CLIENT_INITIALIZER = rpcClientInitializer;
        tmCluster = TxManagerHost.parserList(clientConfig.getManagerAddress());

    }

    /**
     * 重新搜寻TM
     */
    public static void search() {
        Objects.requireNonNull(RPC_CLIENT_INITIALIZER);
        Objects.requireNonNull(tmCluster);
        RPC_CLIENT_INITIALIZER.init(tmCluster);
    }
}
