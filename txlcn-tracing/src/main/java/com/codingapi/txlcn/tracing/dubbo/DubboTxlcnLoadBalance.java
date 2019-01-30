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
package com.codingapi.txlcn.tracing.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tracing.TracingContext;
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
class DubboTxlcnLoadBalance {

    static <T> Invoker<T> chooseInvoker(List<Invoker<T>> invokers, URL url, Invocation invocation, TxLcnLoadBalance loadBalance) {
        TracingContext.tracing()
                .addApp(RpcContext.getContext().getUrl().getPath(), RpcContext.getContext().getLocalAddressString());
        assert invokers.size() > 0;
        String path = invokers.get(0).getUrl().getPath();
        JSONObject appMap = TracingContext.tracing().appMap();
        log.debug("target[{}] invokers: {}", path, invokers);
        Invoker<T> chooseInvoker = null;
        if (appMap.containsKey(path)) {
            for (Invoker<T> tInvoker : invokers) {
                if (tInvoker.getUrl().getAddress().equals(appMap.getString(path))) {
                    chooseInvoker = tInvoker;
                    log.debug("txlcn choosed server [{}] in txGroup: {}", tInvoker, TracingContext.tracing().groupId());
                    break;
                }
            }
        }
        if (chooseInvoker == null) {
            Invoker<T> invoker = loadBalance.select(invokers, url, invocation);
            TracingContext.tracing().addApp(invoker.getUrl().getPath(), invoker.getUrl().getAddress());
            return invoker;
        }
        return chooseInvoker;
    }

    @FunctionalInterface
    public interface TxLcnLoadBalance {
        <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation);
    }
}
