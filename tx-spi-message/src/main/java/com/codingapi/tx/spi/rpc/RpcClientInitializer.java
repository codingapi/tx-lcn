package com.codingapi.tx.spi.rpc;

import com.codingapi.tx.spi.rpc.dto.TxManagerHost;

import java.net.SocketAddress;
import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
public interface RpcClientInitializer {


    /**
     * rpc client init
     * @param hosts
     */
    void init(List<TxManagerHost> hosts);

    /**
     * 建立连接
     * @param socketAddress
     */
    void connect(SocketAddress socketAddress);

}
