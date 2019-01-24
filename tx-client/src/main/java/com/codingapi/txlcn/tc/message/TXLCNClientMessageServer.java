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
package com.codingapi.txlcn.tc.message;

import com.codingapi.txlcn.tc.config.TxClientConfig;
import com.codingapi.txlcn.spi.message.RpcClientInitializer;
import com.codingapi.txlcn.spi.message.dto.TxManagerHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author lorne
 */
@Component
public class TXLCNClientMessageServer {

    private final RpcClientInitializer rpcClientInitializer;

    private final TxClientConfig txClientConfig;

    @Autowired
    public TXLCNClientMessageServer(RpcClientInitializer rpcClientInitializer, TxClientConfig txClientConfig) {
        this.rpcClientInitializer = rpcClientInitializer;
        this.txClientConfig = txClientConfig;
    }

    public void init() throws Exception {
        rpcClientInitializer.init(TxManagerHost.parserList(txClientConfig.getManagerAddress()), new CountDownLatch(0));
    }
}
