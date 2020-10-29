package com.codingapi.txlcn.tc.rpc;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;

/**
 * @author zhanghonglong
 * @date 2020/10/28 11:01
 */
@Activate(group = Constants.PROVIDER)
public class DubboProviderRpcTransactionInterceptor implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcTransactionContext.getInstance().invoke(invocation.getAttachments()::get);
        return invoker.invoke(invocation);
    }
}
