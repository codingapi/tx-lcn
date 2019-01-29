package com.codingapi.txlcn.tracing.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.codingapi.txlcn.tracing.TracingConstants;
import com.codingapi.txlcn.tracing.TracingContext;


/**
 * Description:
 * Date: 19-1-28 下午5:15
 *
 * @author ujued
 */
@Activate(group = Constants.CONSUMER)
public class DubboRequestInterceptor implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext().setAttachment(TracingConstants.HEADER_KEY_GROUP_ID, TracingContext.tracingContext().groupId());
        RpcContext.getContext().setAttachment(TracingConstants.HEADER_KEY_APP_LIST, TracingContext.tracingContext().appListString());
        return invoker.invoke(invocation);
    }
}
