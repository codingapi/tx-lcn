package com.codingapi.tx.manager.core.message;

import com.codingapi.tx.commons.exception.TxManagerException;

/**
 * @author lorne
 * @date 2018/12/2
 * @description LCN分布式事务 manager业务处理
 */
public interface RpcExecuteService {

    /**
     * 执行业务
     *
     * @return
     */
    Object execute(TransactionCmd transactionCmd) throws TxManagerException;

}
