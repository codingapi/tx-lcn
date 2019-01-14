/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.commons.exception;

/**
 * Description:
 * Date: 1/8/19
 *
 * @author ujued
 */
public class TransactionStateException extends Exception {
    public static final int NON_MOD = 10;
    public static final int NON_ASPECT = 11;
    public static final int RPC_ERR = 12;

    private int code;

    public TransactionStateException(String message, int code) {
        super(message);
        this.code = code;
    }

    public TransactionStateException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public TransactionStateException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public TransactionStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
