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

import com.codingapi.txlcn.common.runner.TxLcnInitializer;
import com.codingapi.txlcn.txmsg.RpcClientInitializer;
import com.codingapi.txlcn.txmsg.RpcConfig;
import com.codingapi.txlcn.txmsg.dto.TxManagerHost;
import com.codingapi.txlcn.tc.config.TxClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author lorne
 */
@Component
public class TCRpcServer implements TxLcnInitializer {

    private final RpcClientInitializer rpcClientInitializer;

    private final TxClientConfig txClientConfig;

    private final RpcConfig rpcConfig;

    @Autowired
    public TCRpcServer(RpcClientInitializer rpcClientInitializer,
                       TxClientConfig txClientConfig, RpcConfig rpcConfig) {
        this.rpcClientInitializer = rpcClientInitializer;
        this.txClientConfig = txClientConfig;
        this.rpcConfig = rpcConfig;
    }

    @Override
    public void init() throws Exception {
        // rpc timeout (ms)
        if (rpcConfig.getWaitTime() <= 5) {
            rpcConfig.setWaitTime(1000);
        }

        // rpc client init.
        rpcClientInitializer.init(TxManagerHost.parserList(txClientConfig.getManagerAddress()), false);
    }
}
