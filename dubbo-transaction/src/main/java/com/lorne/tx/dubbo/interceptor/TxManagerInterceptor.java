package com.lorne.tx.dubbo.interceptor;


import com.alibaba.dubbo.rpc.RpcContext;
//import com.lorne.tx.bean.TxTransactionCompensate;
import com.lorne.tx.service.AspectBeforeService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lorne on 2017/6/7.
 */

@Component
public class TxManagerInterceptor {


    @Autowired
    private AspectBeforeService aspectBeforeService;


    public Object around(ProceedingJoinPoint point) throws Throwable {

       // TxTransactionCompensate compensate = TxTransactionCompensate.current();
        String groupId = null;
        int maxTimeOut = 0;
//        if(compensate==null){
//            try {
//                groupId = RpcContext.getContext().getAttachment("tx-group");
//                maxTimeOut = Integer.parseInt(RpcContext.getContext().getAttachment("tx-maxTimeOut"));
//            }catch (Exception e){}
//        }

        try {
            groupId = RpcContext.getContext().getAttachment("tx-group");
            maxTimeOut = Integer.parseInt(RpcContext.getContext().getAttachment("tx-maxTimeOut"));
        }catch (Exception e){}
        return aspectBeforeService.around(groupId,maxTimeOut,point);
    }

}
