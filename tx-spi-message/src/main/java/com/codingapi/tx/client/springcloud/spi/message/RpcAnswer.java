package com.codingapi.tx.client.springcloud.spi.message;


import com.codingapi.tx.client.springcloud.spi.message.dto.RpcCmd;

/**
 * @author lorne
 * @date 2018/12/2
 * @description
 */
public interface RpcAnswer {

    /**
     * 业务处理
     * @param rpcCmd    message 曾业务回调函数
     *
     */
    void callback(RpcCmd rpcCmd);

}
