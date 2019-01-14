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
package com.codingapi.txlcn.spi.message.loadbalance;

import com.codingapi.txlcn.spi.message.exception.RpcException;

/**
 * @author lorne
 */
public interface RpcLoadBalance {

    /**
     * 获取一个远程标识关键字
     * @return 远程key
     * @throws RpcException 远程调用请求异常
     */
    String getRemoteKey()throws RpcException;


}
