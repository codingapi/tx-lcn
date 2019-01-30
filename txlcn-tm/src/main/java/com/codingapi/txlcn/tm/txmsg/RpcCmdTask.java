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
package com.codingapi.txlcn.tm.txmsg;

import com.codingapi.txlcn.txmsg.LCNCmdType;
import com.codingapi.txlcn.txmsg.RpcClient;
import com.codingapi.txlcn.txmsg.dto.MessageDto;
import com.codingapi.txlcn.txmsg.dto.RpcCmd;
import com.codingapi.txlcn.txmsg.exception.RpcException;
import com.codingapi.txlcn.tm.support.TxLcnManagerRpcBeanHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/12
 *
 * @author ujued
 */
@Slf4j
public class RpcCmdTask implements Runnable {

    private final RpcCmd rpcCmd;

    private final TxLcnManagerRpcBeanHelper rpcBeanHelper;

    private final RpcClient rpcClient;

    RpcCmdTask(TxLcnManagerRpcBeanHelper rpcBeanHelper, RpcCmd rpcCmd) {
        this.rpcBeanHelper = rpcBeanHelper;
        this.rpcCmd = rpcCmd;
        this.rpcClient = rpcBeanHelper.getByType(RpcClient.class);
    }

    @Override
    public void run() {

    }


}
