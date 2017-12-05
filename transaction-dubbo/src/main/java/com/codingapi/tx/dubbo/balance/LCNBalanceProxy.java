package com.codingapi.tx.dubbo.balance;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.framework.utils.serializer.SerializerHelper;
import com.lorne.core.framework.utils.encode.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create by lorne on 2017/11/29
 */

public class LCNBalanceProxy {

    private SerializerHelper<Invoker> serializerHelper = new SerializerHelper<>();

    private Logger logger = LoggerFactory.getLogger(LCNBalanceProxy.class);

    public <T> Invoker<T> proxy(Invoker<T> invoker) throws RpcException {

        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        if(txTransactionLocal==null){
            return invoker;
        }

        logger.info("LCNBalanceProxy - > start");

        String groupId = txTransactionLocal.getGroupId();

        String uniqueKey = invoker.getUrl().getServiceInterface();

        logger.info("LCNBalanceProxy - > uniqueKey - >" +uniqueKey);

        String key = MD5Util.md5((groupId + "_" + uniqueKey).getBytes());

        //请求tm获取模块信息
        Invoker old = serializerHelper.parser(txTransactionLocal.getLoadBalance(key),Invoker.class);

        if(old!=null){
            logger.info("LCNBalanceProxy - > load old invoker ");
            return old;
        }

        txTransactionLocal.putLoadBalance(key,serializerHelper.serialize(invoker));

        logger.info("LCNBalanceProxy - > load new invoker ");

        logger.info("LCNBalanceProxy - > end");
        return invoker;
    }
}
