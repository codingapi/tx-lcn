package com.codingapi.tx.motan.interceptor;

import com.codingapi.tx.aop.service.AspectBeforeService;
import com.weibo.api.motan.rpc.RpcContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2017/11/17 15:42
 */
@Component
public class TxManagerInterceptor {

    @Resource
    private AspectBeforeService aspectBeforeService;


    public Object around(ProceedingJoinPoint point) throws Throwable {

        String groupId = null;
        int maxTimeOut = 0;
        try {
            groupId = (String) RpcContext.getContext().getAttribute("tx-group");
            maxTimeOut = Integer.parseInt(RpcContext.getContext().getAttribute("tx-maxTimeOut").toString());
        } catch (Exception e) {
        }
        return aspectBeforeService.around(groupId, maxTimeOut, point);
    }
}
