package com.codingapi.tx.client.spi.message.loadbalance;

import com.codingapi.tx.client.spi.message.exception.RpcException;

/**
 * @author lorne
 * @date 2019/1/5
 * @description
 */
public interface RpcLoadBalance {

    /**
     * 获取一个远程标识关键字
     * @return
     * @throws RpcException
     */
    String getRemoteKey()throws RpcException;


}
