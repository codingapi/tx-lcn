package com.codingapi.tx.dubbo.balance;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.lorne.core.framework.utils.encode.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * create by lorne on 2017/11/29
 */

public class LCNBalanceProxy {

    private Logger logger = LoggerFactory.getLogger(LCNBalanceProxy.class);

    public <T> Invoker<T> proxy(List<Invoker<T>> invokers,Invoker<T> invoker) throws RpcException {
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        if(txTransactionLocal==null){
            return invoker;
        }

        try {
            logger.info("LCNBalanceProxy - > start");

            String groupId = txTransactionLocal.getGroupId();

            String uniqueKey = invoker.getUrl().getServiceInterface();

            logger.info("LCNBalanceProxy - > uniqueKey - >" + uniqueKey);

            String key = MD5Util.md5((groupId + "_" + uniqueKey).getBytes());

            //请求tm获取模块信息
            Invoker old = getInvoker(txTransactionLocal, invokers, key);

            if (old != null) {
                logger.info("LCNBalanceProxy - > load old invoker ");

                return old;
            }
            putInvoker(key, txTransactionLocal, invoker);

            logger.info("LCNBalanceProxy - > load new invoker ");

            return invoker;
        }finally {
            logger.info("LCNBalanceProxy - > end");
        }
    }


    private void putInvoker(String key,TxTransactionLocal txTransactionLocal,Invoker invoker){
        String serviceName =  invoker.getUrl().getServiceInterface();
        String address = invoker.getUrl().getAddress();

        String md5 = MD5Util.md5((address+serviceName).getBytes());

        logger.info("putInvoker->address->"+address+",md5-->"+md5);

        txTransactionLocal.putLoadBalance(key,md5);
    }


    private <T> Invoker<T> getInvoker(TxTransactionLocal txTransactionLocal,List<Invoker<T>> invokers,String key){
        String val = txTransactionLocal.getLoadBalance(key);
        if(StringUtils.isEmpty(val)){
            return null;
        }
        for(Invoker<T> invoker:invokers){
           String serviceName =  invoker.getUrl().getServiceInterface();
           String address = invoker.getUrl().getAddress();

           String md5 = MD5Util.md5((address+serviceName).getBytes());

           logger.info("getInvoker->address->"+address+",md5-->"+md5);

           if(val.equals(md5)){
               return invoker;
           }
        }
        return null;
    }
}
