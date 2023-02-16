package com.codingapi.tx.aop.service.impl;

import com.codingapi.tx.annotation.TxTransaction;
import com.codingapi.tx.annotation.TxTransactionMode;
import com.codingapi.tx.aop.bean.TxTransactionInfo;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.aop.service.AspectBeforeService;
import com.codingapi.tx.aop.service.TransactionServer;
import com.codingapi.tx.aop.service.TransactionServerFactoryService;
import com.codingapi.tx.model.TransactionInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * Created by lorne on 2017/7/1.
 */
@Service
public class AspectBeforeServiceImpl implements AspectBeforeService {

    @Autowired
    private TransactionServerFactoryService transactionServerFactoryService;


    private Logger logger = LoggerFactory.getLogger(AspectBeforeServiceImpl.class);

    @Override
    public Object around(String groupId, ProceedingJoinPoint point, String mode) throws Throwable {

        MethodSignature signature = (MethodSignature) point.getSignature();
        //获取接口执行方法
        Method method = signature.getMethod();
        Class<?> clazz = point.getTarget().getClass();
        //获取方法参数
        Object[] args = point.getArgs();
        //获取具体执行的子类方法
        Method thisMethod = clazz.getMethod(method.getName(), method.getParameterTypes());

        //获取分布式事务注解
        TxTransaction transaction = thisMethod.getAnnotation(TxTransaction.class);
        //记录事务全局上下文信息
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();

        logger.debug("around--> groupId-> " +groupId+",txTransactionLocal->"+txTransactionLocal);
        //封装事务方法
        TransactionInvocation invocation = new TransactionInvocation(clazz, thisMethod.getName(), thisMethod.toString(), args, method.getParameterTypes());

        //封装事务信息，包含注解信息，方法信息，事务组id。
        TxTransactionInfo info = new TxTransactionInfo(transaction,txTransactionLocal,invocation,groupId);
        try {
            //设置事务类型
            info.setMode(TxTransactionMode.valueOf(mode));
        } catch (Exception e) {
            info.setMode(TxTransactionMode.TX_MODE_LCN);//没设置模式则默认lcn
        }
        //创建事务服务
        TransactionServer server = transactionServerFactoryService.createTransactionServer(info);

        //执行核心事务方法
        return server.execute(point, info);
    }
}
