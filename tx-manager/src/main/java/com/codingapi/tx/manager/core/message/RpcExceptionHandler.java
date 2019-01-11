package com.codingapi.tx.manager.core.message;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
public interface RpcExceptionHandler {

    /**
     * 通知事务单元业务异常
     *
     * @param params
     * @param e
     */
    void handleNotifyUnitBusinessException(Object params, Throwable e);

    /**
     * 通知事务单元通讯异常
     *
     * @param params
     * @param e
     */
    void handleNotifyUnitMessageException(Object params, Throwable e);
}
