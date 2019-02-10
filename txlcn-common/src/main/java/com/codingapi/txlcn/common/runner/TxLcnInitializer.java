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
package com.codingapi.txlcn.common.runner;

/**
 * Description: TxLcn run control
 * Company: CodingApi
 * Date: 2019/1/16
 *
 * @author codingapi meetzy
 */
public interface TxLcnInitializer {
    
    /**
     * init
     *
     * @throws Exception Throwable
     */
    default void init() throws Exception {
    }
    
    /**
     * destroy
     *
     * @throws Exception Throwable
     */
    default void destroy() throws Exception {
    }
    
    /**
     * order
     *
     * @return int
     */
    default int order() {
        return 0;
    }
}
