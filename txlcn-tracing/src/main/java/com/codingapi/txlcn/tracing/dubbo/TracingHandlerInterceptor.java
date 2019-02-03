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

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.codingapi.txlcn.common.util.Maps;
import com.codingapi.txlcn.tracing.TracingConstants;
import com.codingapi.txlcn.tracing.TracingContext;

/**
 * Description: 接收Tracing需要的数据
 * Date: 19-1-28 下午5:08
 *
 * @author ujued
 */
@Activate(group = {Constants.PROVIDER})
public class TracingHandlerInterceptor implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String groupId = invocation.getAttachment(TracingConstants.HEADER_KEY_GROUP_ID, "");
        String appList = invocation.getAttachment(TracingConstants.HEADER_KEY_APP_MAP, "");
        TracingContext.tracing().init(Maps.newHashMap(TracingConstants.GROUP_ID, groupId, TracingConstants.APP_MAP, appList));
        return invoker.invoke(invocation);
    }
}
