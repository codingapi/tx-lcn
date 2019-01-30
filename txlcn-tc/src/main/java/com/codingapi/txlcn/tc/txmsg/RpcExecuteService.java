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
package com.codingapi.txlcn.tc.txmsg;


import com.codingapi.txlcn.common.exception.TxClientException;

import java.io.Serializable;

/**
 *  LCN分布式事务资源控制
 * @author lorne
 */
public interface RpcExecuteService {

    /**
     * 执行业务
     * @param  transactionCmd transactionCmd
     * @throws  TxClientException TxClientException
     * @return object
     */
    Serializable execute(TransactionCmd transactionCmd) throws TxClientException;

}
