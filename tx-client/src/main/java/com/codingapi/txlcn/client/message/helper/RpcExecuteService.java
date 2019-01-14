package com.codingapi.txlcn.client.message.helper;


import com.codingapi.txlcn.commons.exception.TxClientException;

/**
 * @author lorne
 * @date 2018/12/2
 * @description LCN分布式事务资源控制
 */
public interface RpcExecuteService {

    /**
     * 执行业务
     */
    Object execute(TransactionCmd transactionCmd) throws TxClientException;

}
