package com.codingapi.txlcn.tc.rpc;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @author zhanghonglong
 * @date 2020/10/28 11:01
 */
@Activate(group = CommonConstants.CONSUMER)
@Slf4j
public class ApacheDubboRpcTransactionInterceptor implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcTransactionContext.getInstance().build(invocation.getAttachments()::put);
        return invoker.invoke(invocation);
    }
}
