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
 * Company: CodingApi
 * Date: 2018/12/27
 *
 * @author codingapi
 */
public class JoinGroupException extends Exception {

    public JoinGroupException() {
    }

    public JoinGroupException(String message) {
        super(message);
    }

    public JoinGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public JoinGroupException(Throwable cause) {
        super(cause);
    }

    public JoinGroupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
