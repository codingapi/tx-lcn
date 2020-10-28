package com.codingapi.txlcn.tc.rpc;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanghonglong
 * @date 2020/10/28 11:01
 */
@Activate(group = Constants.CONSUMER)
@Slf4j
public class AlibabaDubboRpcTransactionInterceptor implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcTransactionContext.getInstance().build(invocation.getAttachments()::put);
        return invoker.invoke(invocation);
    }
}
