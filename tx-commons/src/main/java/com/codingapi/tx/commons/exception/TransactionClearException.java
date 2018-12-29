package com.codingapi.tx.commons.exception;

import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 2018/12/11
 *
 * @author ujued
 */
@NoArgsConstructor
public class TransactionClearException extends Exception {
    public TransactionClearException(String message) {
        super(message);
    }

    public TransactionClearException(Throwable ex) {
        super(ex);
    }
}
