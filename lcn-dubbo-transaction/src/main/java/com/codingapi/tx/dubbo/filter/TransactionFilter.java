package com.codingapi.tx.dubbo.filter;

import com.alibaba.dubbo.rpc.*;
import com.codingapi.tx.bean.TxTransactionLocal;

/**
 * Created by lorne on 2017/6/30.
 */
public class TransactionFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        if(txTransactionLocal!=null){
            RpcContext.getContext().setAttachment("tx-group",txTransactionLocal.getGroupId());
            RpcContext.getContext().setAttachment("tx-maxTimeOut",String.valueOf(txTransactionLocal.getMaxTimeOut()));
        }
        return invoker.invoke(invocation);
    }
}
