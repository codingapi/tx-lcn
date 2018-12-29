package com.codingapi.tx.client.framework.control;

import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.commons.exception.BeforeBusinessException;
import com.codingapi.tx.commons.exception.TxClientException;

/**
 * @author lorne
 * @date 2018/12/2
 * @description LCN分布式事务控制
 */
public interface LCNTransactionControl {

    /**
     * 业务代码执行前
     *
     * @param info
     */
    default void preBusinessCode(TxTransactionInfo info)throws BeforeBusinessException {

    }

    /**
     * 执行业务代码
     *
     * @param info
     * @throws Throwable
     */
    default Object doBusinessCode(TxTransactionInfo info) throws Throwable {
        return info.getJoinPoint().proceed();
    }


    /**
     * 业务代码执行失败
     *
     * @param info
     * @param throwable
     */
    default void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {

    }

    /**
     * 业务代码执行成功
     *
     * @param info
     */
    default void onBusinessCodeSuccess(TxTransactionInfo info, Object result) throws TxClientException {

    }

    /**
     * 清场
     *
     * @param info
     */
    default void postBusinessCode(TxTransactionInfo info){

    }
}
