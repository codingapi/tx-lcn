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
package com.codingapi.txlcn.tm.support;


import com.codingapi.txlcn.txmsg.LCNCmdType;
import com.codingapi.txlcn.tm.txmsg.RpcExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * BeanName 获取工具类
 * @author lorne
 */
@Component
public class TxLcnManagerRpcBeanHelper {


    /**
     * manager bean 名称格式
     * manager_%s_%s
     * manager:前缀 %s:业务处理(create,add,close)
     */
    private static final String RPC_BEAN_NAME_FORMAT = "rpc_%s";


    @Autowired
    private ApplicationContext spring;


    public String getServiceBeanName(LCNCmdType cmdType) {
        return String.format(RPC_BEAN_NAME_FORMAT, cmdType.getCode());
    }


    public RpcExecuteService loadManagerService(LCNCmdType cmdType) {
        return spring.getBean(getServiceBeanName(cmdType), RpcExecuteService.class);
    }

    public <T> T getByType(Class<T> type) {
        return spring.getBean(type);
    }
}
