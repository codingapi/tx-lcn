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
package com.codingapi.txlcn.client.message;

import com.codingapi.txlcn.client.message.helper.RpcExecuteService;
import com.codingapi.txlcn.client.message.helper.TransactionCmd;
import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.exception.TxClientException;
import com.codingapi.txlcn.spi.message.params.NotifyConnectParams;
import com.codingapi.txlcn.spi.message.RpcClientInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@Service(value = "rpc_notify-connect")
@Slf4j
public class RpcNotifyConnectService implements RpcExecuteService {

    private final RpcClientInitializer rpcClientInitializer;

    @Autowired
    public RpcNotifyConnectService(RpcClientInitializer rpcClientInitializer) {
        this.rpcClientInitializer = rpcClientInitializer;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxClientException {
        try {

            log.info("transactionCmd->{}", transactionCmd);

            NotifyConnectParams notifyConnectParams = transactionCmd.getMsg().loadData(NotifyConnectParams.class);

            log.info("notifyConnectParams->{}", notifyConnectParams);

            rpcClientInitializer.connect(new InetSocketAddress(notifyConnectParams.getHost(), notifyConnectParams.getPort()));
        } catch (SerializerException e) {
            throw new TxClientException(e);
        }

        return null;
    }
}
