package com.lorne.tx.springcloud.interceptor;

//import com.lorne.tx.bean.TxTransactionCompensate;
import com.lorne.tx.service.AspectBeforeService;
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

       // TxTransactionCompensate compensate = TxTransactionCompensate.current();
        String groupId = null;
        int maxTimeOut = 0;
       // if (compensate == null) {
            try {
                RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
                HttpServletRequest request = requestAttributes == null ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
                groupId = request == null ? null : request.getHeader("tx-group");
                maxTimeOut = request == null?0:Integer.parseInt(request.getHeader("tx-maxTimeOut"));
            }catch (Exception e){}
       // }
        return aspectBeforeService.around(groupId,maxTimeOut, point);
    }
}
