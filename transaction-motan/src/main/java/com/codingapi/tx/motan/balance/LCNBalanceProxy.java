package com.codingapi.tx.motan.balance;

import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.lorne.core.framework.utils.encode.MD5Util;
import com.weibo.api.motan.rpc.Referer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>LCN负载均衡代理</p>
 *
 * @author 张峰 zfvip_it@163.com
 * 2017/12/1 10:21
 */
public class LCNBalanceProxy {

    private Logger logger = LoggerFactory.getLogger(LCNBalanceProxy.class);

    protected Referer proxy(List<Referer> referers,Referer referer) {
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        if (txTransactionLocal == null) {
            return referer;
        }

        try {
            logger.debug("LCNBalanceProxy - > start");

            String groupId = txTransactionLocal.getGroupId();

            String uniqueKey = referer.getInterface().getName();

            logger.debug("LCNBalanceProxy - > uniqueKey - >" + uniqueKey);

            String key = MD5Util.md5((groupId + "_" + uniqueKey).getBytes());

            Referer old = getReferer(txTransactionLocal,referers,key);
            if (old != null) {
                logger.debug("LCNBalanceProxy - > load old referer ");

                return old;
            }

            putReferer(key,txTransactionLocal,referer);

            logger.debug("LCNBalanceProxy - > load new referer ");

            return referer;
        }finally {
            logger.debug("LCNBalanceProxy - > end");
        }
    }


    private void putReferer(String key,TxTransactionLocal txTransactionLocal,Referer referer){
        String serviceName = referer.getInterface().getName();
        String address = referer.getUrl().getHost()+":"+referer.getUrl().getPort();

        String md5 = MD5Util.md5((address+serviceName).getBytes());

        logger.debug("putReferer->address->"+address+",md5-->"+md5);

        txTransactionLocal.putLoadBalance(key,md5);
    }


    private  Referer getReferer(TxTransactionLocal txTransactionLocal,List<Referer> referers,String key){
        String val = txTransactionLocal.getLoadBalance(key);
        if(StringUtils.isEmpty(val)){
            return null;
        }
        for(Referer invoker:referers){
            String serviceName = invoker.getInterface().getName();
            String address = invoker.getUrl().getHost()+":"+invoker.getUrl().getPort();

            String md5 = MD5Util.md5((address+serviceName).getBytes());

            logger.debug("getReferer->address->"+address+",md5-->"+md5);

            if(val.equals(md5)){
                return invoker;
            }
        }
        return null;
    }
}
