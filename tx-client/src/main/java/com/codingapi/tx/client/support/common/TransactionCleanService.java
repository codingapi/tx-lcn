package com.codingapi.tx.client.support.common;

import com.codingapi.tx.commons.exception.TransactionClearException;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
public interface TransactionCleanService {

    /**
     * 事务清理业务
     *
     * @param groupId
     * @param state    事务状态 1 提交 0 回滚
     * @param unitId
     * @param unitType 事务类型
     * @throws TransactionClearException
     */
    void clear(String groupId, int state, String unitId, String unitType) throws TransactionClearException;
}
