package com.codingapi.tx.dubbo.balance;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create by lorne on 2017/11/29
 */

public class LCNBalanceProxy {


    private Logger logger = LoggerFactory.getLogger(LCNBalanceProxy.class);

    public <T> Invoker<T> proxy(Invoker<T> invoker) throws RpcException {
        logger.info("LCNBalanceProxy - > start");

        logger.info("LCNBalanceProxy - > end");
        return invoker;
    }
}
