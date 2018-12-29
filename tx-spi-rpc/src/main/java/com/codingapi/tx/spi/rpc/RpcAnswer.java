package com.codingapi.tx.spi.rpc;


import com.codingapi.tx.spi.rpc.dto.RpcCmd;

/**
 * @author lorne
 * @date 2018/12/2
 * @description
 */
public interface RpcAnswer {

    /**
     * 业务处理
     * @param rpcCmd    rpc 曾业务回调函数
     *
     */
    void callback(RpcCmd rpcCmd);

}
