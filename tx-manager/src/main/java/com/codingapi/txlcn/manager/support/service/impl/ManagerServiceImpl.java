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
package com.codingapi.txlcn.manager.support.service.impl;

import com.codingapi.txlcn.spi.message.params.NotifyConnectParams;
import com.codingapi.txlcn.manager.support.service.ManagerService;
import com.codingapi.txlcn.manager.core.message.MessageCreator;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@Service
public class ManagerServiceImpl implements ManagerService {


    @Autowired
    private RpcClient rpcClient;


    @Override
    public boolean refresh( NotifyConnectParams notifyConnectParams) throws RpcException {
        List<String> keys =  rpcClient.loadAllRemoteKey();
        if(keys!=null&&keys.size()>0){
            for(String key:keys){
                rpcClient.send(key, MessageCreator.newTxManager(notifyConnectParams));
            }
        }
        return true;
    }
}
