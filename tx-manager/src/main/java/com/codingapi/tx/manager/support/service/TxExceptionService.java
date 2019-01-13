package com.codingapi.tx.manager.support.service;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.commons.exception.TransactionStateException;
import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.manager.support.restapi.model.ExceptionList;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
public interface TxExceptionService {

    /**
     * 写补偿记录
     *
     * @param writeTxExceptionReq
     */
    void writeTxException(WriteTxExceptionDTO writeTxExceptionReq);


    /**
     * 获取事务状态
     *
     * @param groupId
     * @return
     */
    int transactionState(String groupId);

    /**
     * 获取补偿列表
     *
     * @param page
     * @param limit
     * @return
     */
    ExceptionList exceptionList(Integer page, Integer limit, String keyword, int registrar);

    /**
     * Client 切面信息
     *
     * @param groupId
     * @param unitId
     * @return
     */
    JSONObject getTransactionInfo(String groupId, String unitId) throws TxManagerException, TransactionStateException;
}
