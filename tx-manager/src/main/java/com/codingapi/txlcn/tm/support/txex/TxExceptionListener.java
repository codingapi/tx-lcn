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
package com.codingapi.txlcn.tm.support.txex;

import com.codingapi.txlcn.tm.support.db.domain.TxException;

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
     * @param txException txException
     */
    void onException(TxException txException);
}
