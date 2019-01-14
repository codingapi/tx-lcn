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
package com.codingapi.txlcn.client.dubbo.spi.sleuth.loadbalance;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.codingapi.txlcn.spi.sleuth.listener.SleuthParamListener;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/14
 *
 * @author ujued
 */
@Slf4j
public class TXLCNLoadBalance {
    
    public static SleuthParamListener sleuthParamListener;
    
    static <T> Invoker<T> chooseInvoker(List<Invoker<T>> invokers, URL url, Invocation invocation, TxLcnLoadBalance loadBalance) {
        String localKey = RpcContext.getContext().getLocalAddressString();
        List<String> appList = sleuthParamListener.beforeBalance(localKey);
        Invoker<T> chooseInvoker = null;
        for (Invoker<T> tInvoker : invokers) {
            String serverKey = tInvoker.getUrl().getAddress();
            for (String appKey : appList) {
                if (appKey.equals(serverKey)) {
                    chooseInvoker = tInvoker;
                }
            }
        }
        if (chooseInvoker == null) {
            Invoker<T> invoker = loadBalance.select(invokers, url, invocation);
            sleuthParamListener.afterNewBalance(invoker.getUrl().getAddress());
            return invoker;
        } else {
            return chooseInvoker;
        }
        
    }
    
    @FunctionalInterface
    public interface TxLcnLoadBalance {
        
        <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation);
        
    }
}
