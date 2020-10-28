package com.codingapi.txlcn.tc.rpc;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;

/**
 * @author zhanghonglong
 * @date 2020/10/28 11:01
 */
@Activate(group = Constants.CONSUMER)
public class DubboConsumerRpcTransactionInterceptor implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcTransactionContext.getInstance().build(invocation.getAttachments()::put);
        return invoker.invoke(invocation);
    }
}
