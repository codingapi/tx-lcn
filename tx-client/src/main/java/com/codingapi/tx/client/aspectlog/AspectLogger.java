package com.codingapi.tx.client.aspectlog;

import com.codingapi.tx.commons.bean.TransactionInfo;

/**
 * Description: 切面日志操作
 * Date: 2018/12/28
 *
 * @author ujued
 */
public interface AspectLogger {

    /**
     * 记录切面日志
     *
     * @param groupId
     * @param unitId
     * @param transactionInfo 切面信息
     */
    void trace(String groupId, String unitId, TransactionInfo transactionInfo);

    /**
     * 清理切面日志
     *
     * @param groupId
     * @param unitId
     */
    void clearLog(String groupId, String unitId);
}
