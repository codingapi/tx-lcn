package com.codingapi.tx.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lorne on 2017/6/30.
 */
@Activate(group = Constants.CONSUMER, order = -10001)
public class TransactionFilter implements Filter {


    private Logger logger = LoggerFactory.getLogger(TransactionFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        String groupId = txTransactionLocal == null ? null : txTransactionLocal.getGroupId();

        logger.info("LCN-dubbo TxGroup info -> groupId:"+groupId);

        if(txTransactionLocal!=null){
            RpcContext.getContext().setAttachment("tx-group",groupId);
            RpcContext.getContext().setAttachment("tx-mode",txTransactionLocal.getMode().name());
        }

        return invoker.invoke(invocation);
    }
}
