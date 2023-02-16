package com.codingapi.tx.springcloud.interceptor;

import com.codingapi.tx.aop.service.AspectBeforeService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lorne on 2017/6/7.
 */

@Component
public class TxManagerInterceptor {

    @Autowired
    private AspectBeforeService aspectBeforeService;

    public Object around(ProceedingJoinPoint point) throws Throwable {
        String groupId = null;
        String mode = null;
        try {
            //如果是发起方，这里都是null。
            //如果是调用方，则这里会有发起方通过feign发送过来的事务组id和事务模式。
            //事务组id：事务唯一标识。
            //事务模式：包括lcn，或者tcc等。这里一般为null。
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = requestAttributes == null ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
            groupId = request == null ? null : request.getHeader("tx-group");
            mode = request == null ? null : request.getHeader("tx-mode");
        }catch (Exception e){}
        //继续调用切面方法
        return aspectBeforeService.around(groupId, point, mode);
    }
}
