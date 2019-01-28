package com.codingapi.txlcn.tracing.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.codingapi.txlcn.commons.util.Maps;
import com.codingapi.txlcn.tracing.TracingConstants;
import com.codingapi.txlcn.tracing.TracingContext;

/**
 * Description:
 * Date: 19-1-28 下午5:08
 *
 * @author ujued
 */
@Activate(group = {Constants.PROVIDER})
public class TracingHandlerInterceptor implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String groupId = invocation.getAttachment(TracingConstants.HEADER_KEY_GROUP_ID, "");
        String appList = invocation.getAttachment(TracingConstants.HEADER_KEY_APP_LIST, "");
        TracingContext.tracingContext().init(Maps.newHashMap(TracingConstants.GROUP_ID, groupId, TracingConstants.APP_LIST, appList));
        return invoker.invoke(invocation);
    }
}
