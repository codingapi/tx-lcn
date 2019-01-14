package com.codingapi.txlcn.client.support.checking;

import com.codingapi.txlcn.commons.exception.BeforeBusinessException;
import com.codingapi.txlcn.commons.exception.TxClientException;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
public interface DTXExceptionHandler {


    /**
     * 处理创建事务组TxManager业务异常
     *
     * @param params
     * @param ex
     */
    void handleCreateGroupBusinessException(Object params, Throwable ex) throws BeforeBusinessException;

    /**
     * 处理创建事务组TxManager通信异常
     *
     * @param params
     * @param ex
     */
    void handleCreateGroupMessageException(Object params, Throwable ex)throws BeforeBusinessException;


    /**
     * 加入事务组TxManager业务异常
     *
     * @param params
     * @param ex
     */
    void handleJoinGroupBusinessException(Object params, Throwable ex) throws TxClientException;

    /**
     * 加入事务组TxManager通讯异常
     *
     * @param params
     * @param ex
     */
    void handleJoinGroupMessageException(Object params, Throwable ex) throws TxClientException;

    /**
     * 通知事务组业务异常处理
     *
     * @param params
     * @param ex
     */
    void handleNotifyGroupBusinessException(Object params, Throwable ex);

    /**
     * 通知事务组消息异常处理
     *
     * @param params
     * @param ex
     */
    void handleNotifyGroupMessageException(Object params, Throwable ex);

}
