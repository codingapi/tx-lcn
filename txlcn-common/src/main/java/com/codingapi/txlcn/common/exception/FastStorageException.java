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
package com.codingapi.txlcn.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description:
 * Date: 19-1-21 下午3:28
 *
 * @author ujued
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FastStorageException extends Exception {

    public static final int EX_CODE_REPEAT_GROUP = 100;

    public static final int EX_CODE_NON_GROUP = 101;

    /**
     * about lock
     */
    public static final int EX_CODE_REPEAT_LOCK = 201;
    public static final int EX_CODE_ACQUIRE_ERROR = 202;

    public static final int EX_CODE_NON_MACHINE_ID = 301;

    private int code;


    public FastStorageException(int code) {
        this.code = code;
    }

    public FastStorageException(String message, int code) {
        super(message);
        this.code = code;
    }

    public FastStorageException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public FastStorageException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public FastStorageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    @Override
    public String toString() {
        return super.toString() + "(code=" + code + ')';
    }

    public static boolean aboutLock(int code) {
        return String.valueOf(code).charAt(0) == '2';
    }
}
