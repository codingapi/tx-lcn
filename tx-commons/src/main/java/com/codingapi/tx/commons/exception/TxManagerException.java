package com.codingapi.tx.commons.exception;

import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 2018/12/11
 *
 * @author ujued
 */
@NoArgsConstructor
public class TxManagerException extends Exception {
    public TxManagerException(String message) {
        super(message);
    }

    public TxManagerException(Throwable cause) {
        super(cause);
    }
}
