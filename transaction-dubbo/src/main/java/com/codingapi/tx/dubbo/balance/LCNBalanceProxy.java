package com.codingapi.tx.dubbo.balance;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.codingapi.tx.Constants;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create by lorne on 2017/11/29
 */

public class LCNBalanceProxy {

    private Logger logger = LoggerFactory.getLogger(LCNBalanceProxy.class);

    public <T> Invoker<T> proxy(Invoker<T> invoker) throws RpcException {

        logger.info("LCNBalanceProxy - > map-size - >" +Constants.cacheModelInfo.size());
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        if(txTransactionLocal==null){
            return invoker;
        }

        logger.info("LCNBalanceProxy - > start");

        String groupId = txTransactionLocal.getGroupId();

        String uniqueKey = invoker.getUrl().getServiceInterface();

        logger.info("LCNBalanceProxy - > uniqueKey - >" +uniqueKey);

        String key = groupId+"_"+uniqueKey;

        Invoker old = (Invoker) Constants.cacheModelInfo.get(key);

        if(old!=null){
            logger.info("LCNBalanceProxy - > load old invoker ");
            return old;
        }
        Constants.cacheModelInfo.put(key,invoker);

        logger.info("LCNBalanceProxy - > load new invoker ");

        logger.info("LCNBalanceProxy - > end");
        return invoker;
    }
}
