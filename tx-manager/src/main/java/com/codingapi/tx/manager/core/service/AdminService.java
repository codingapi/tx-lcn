package com.codingapi.tx.manager.core.service;

import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.manager.core.restapi.model.DTXInfo;
import com.codingapi.tx.manager.core.restapi.model.TxLogList;
import com.codingapi.tx.manager.core.restapi.model.TxManagerInfo;

/**
 * Description:
 * Date: 2018/12/28
 *
 * @author ujued
 */
public interface AdminService {

    /**
     * 登陆
     *
     * @param password
     * @return
     */
    String login(String password) throws TxManagerException;

    /**
     * 查询TX 日志
     *
     * @param page
     * @param limit
     * @return
     */
    TxLogList txLogList(Integer page, Integer limit);

    /**
     * 分布式事务统计信息
     *
     * @return
     */
    DTXInfo dtxInfo();

    /**
     * 获取TxManager信息
     *
     * @return
     */
    TxManagerInfo getTxManagerInfo();
}
