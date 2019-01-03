package com.codingapi.tx.manager.support.txex;

import com.codingapi.tx.manager.db.domain.TxException;

/**
 * Description:
 * Date: 19-1-3 上午9:40
 *
 * @author ujued
 */
public interface TxExceptionListener {

    /**
     * 实务异常时
     *
     * @param txException
     */
    void onException(TxException txException);
}
