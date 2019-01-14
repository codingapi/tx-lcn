package com.codingapi.txlcn.client.aspect.weave;

import com.codingapi.txlcn.client.bean.DTXLocal;
import com.codingapi.txlcn.client.support.LCNTransactionBeanHelper;
import com.codingapi.txlcn.client.support.resouce.TransactionResourceExecutor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Objects;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/2
 *
 * @author lorne
 */
@Component
@Slf4j
public class DTXResourceWeaver {

    private final LCNTransactionBeanHelper transactionBeanHelper;

    @Autowired
    public DTXResourceWeaver(LCNTransactionBeanHelper transactionBeanHelper) {
        this.transactionBeanHelper = transactionBeanHelper;
    }

    public Object around(ProceedingJoinPoint point) throws Throwable {
        DTXLocal dtxLocal = DTXLocal.cur();
        if (Objects.nonNull(dtxLocal) && dtxLocal.isProxy()) {
            String transactionType = dtxLocal.getTransactionType();
            TransactionResourceExecutor transactionResourceExecutor = transactionBeanHelper.loadTransactionResourceExecuter(transactionType);
            Connection connection = transactionResourceExecutor.proxyConnection(() -> {
                try {
                    return (Connection) point.proceed();
                } catch (Throwable throwable) {
                    throw new IllegalStateException(throwable);
                }
            });
            log.info("proxy a sql connection: {}.", connection);
            return connection;
        }
        return point.proceed();
    }
}
