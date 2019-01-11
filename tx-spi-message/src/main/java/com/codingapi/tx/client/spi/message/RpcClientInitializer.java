package com.codingapi.tx.client.spi.message;

import com.codingapi.tx.client.spi.message.dto.TxManagerHost;

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
     * message client init
     * @param hosts
     */
    void init(List<TxManagerHost> hosts);

    /**
     * 建立连接
     * @param socketAddress
     */
    void connect(SocketAddress socketAddress);

}
