package com.codingapi.tx.motan.balance;

import com.codingapi.tx.Constants;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.weibo.api.motan.rpc.Referer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>LCN负载均衡代理</p>
 *
 * @author 张峰 zfvip_it@163.com
 * 2017/12/1 10:21
 */
public class LCNBalanceProxy<T> {

    private Logger logger = LoggerFactory.getLogger(LCNBalanceProxy.class);

    protected Referer<T> proxy(Referer<T> referer) {
        logger.info("LCNBalanceProxy - > map-size - >" + Constants.cacheModelInfo.size());
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        if (txTransactionLocal == null) {
            return referer;
        }


        logger.info("LCNBalanceProxy - > start");

        String groupId = txTransactionLocal.getGroupId();

        String uniqueKey = referer.getInterface().getName();

        logger.info("LCNBalanceProxy - > uniqueKey - >" + uniqueKey);

        String key = groupId + "_" + uniqueKey;

        Referer old = (Referer) Constants.cacheModelInfo.get(key);

        if (old != null) {
            logger.info("LCNBalanceProxy - > load old referer ");
            return old;
        }
        Constants.cacheModelInfo.put(key, referer);

        logger.info("LCNBalanceProxy - > load new referer ");

        logger.info("LCNBalanceProxy - > end");
        return referer;
    }
}
