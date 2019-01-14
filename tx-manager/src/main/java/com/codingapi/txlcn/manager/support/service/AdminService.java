package com.codingapi.txlcn.manager.support.service;

import com.codingapi.txlcn.commons.exception.TxManagerException;
import com.codingapi.txlcn.manager.support.restapi.model.DTXInfo;
import com.codingapi.txlcn.manager.support.restapi.model.TxLogList;
import com.codingapi.txlcn.manager.support.restapi.model.TxManagerInfo;

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
     * @param groupId
     * @param tag
     * @param timeOrder 时间排序1 顺序 2 逆序
     * @return
     */
    TxLogList txLogList(Integer page, Integer limit, String groupId, String tag, Integer timeOrder);

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
