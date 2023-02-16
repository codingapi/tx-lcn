package com.codingapi.tx.aop.service.impl;

import com.codingapi.tx.aop.bean.TxTransactionInfo;
import com.codingapi.tx.aop.service.TransactionServer;
import com.codingapi.tx.aop.service.TransactionServerFactoryService;
import com.codingapi.tx.datasource.ILCNTransactionControl;
import com.codingapi.tx.framework.utils.SocketManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lorne
 * @date 2017/6/8
 */
@Service
public class TransactionServerFactoryServiceImpl implements TransactionServerFactoryService {

    private Logger logger = LoggerFactory.getLogger(TransactionServerFactoryServiceImpl.class);

    @Autowired
    private TransactionServer txStartTransactionServer;

    @Autowired
    private TransactionServer txRunningTransactionServer;

    @Autowired
    private TransactionServer txDefaultTransactionServer;

    @Autowired
    private TransactionServer txRunningNoTransactionServer;

    @Autowired
    private ILCNTransactionControl transactionControl;

    @Override
    public TransactionServer createTransactionServer(TxTransactionInfo info) throws Throwable {
        if (!SocketManager.getInstance().isNetState()) {
            //检查socket通讯是否正常 （第一次执行时启动txRunningTransactionServer的业务处理控制，然后嵌套调用其他事务的业务方法时都并到txInServiceTransactionServer业务处理下）
            logger.warn("tx-manager not connected.");
            //默认事务执行，不以分布式事务执行。就简单的执行。
            return txDefaultTransactionServer;
        }

        /*********分布式事务处理逻辑***********/
        logger.info("分布式事务处理逻辑...开始");

        /** 事务发起方：仅当TxTransaction注解不为空，其他都为空时。表示分布式事务开始启动 **/
        //注解不为空，且注解声明了为发起方，且事务组id为空，这就说明为事务发起方。同时txTransactionLocal记录全局上下文信息的为空。事务组id为空。
        if (info.getTxTransaction() != null && info.getTxTransaction().isStart() && info.getTxTransactionLocal() == null && StringUtils.isEmpty(info.getTxGroupId())) {
            //检查socket通讯是否正常 （当启动事务的主业务方法执行完以后，再执行其他业务方法时将进入txInServiceTransactionServer业务处理）
            if (SocketManager.getInstance().isNetState()) {
                return txStartTransactionServer;
            } else {
                logger.warn("tx-manager not connected.");
                return txDefaultTransactionServer;
            }
        }

        /** 事务参与方：分布式事务已经开启，业务进行中 **/
        logger.debug("事务参与方：分布式事务已经开启，业务进行中");
        //事务组id不为空，则说明是参与者。
        if (info.getTxTransactionLocal() != null || StringUtils.isNotEmpty(info.getTxGroupId())) {
            //检查socket通讯是否正常 （第一次执行时启动txRunningTransactionServer的业务处理控制，然后嵌套调用其他事务的业务方法时都并到txInServiceTransactionServer业务处理下）
            if (SocketManager.getInstance().isNetState()) {
                if (info.getTxTransactionLocal() != null) {
                    return txDefaultTransactionServer;
                } else {
                    /** 表示整个应用没有获取过DB连接 || 无事务业务的操作 **/
                    if (transactionControl.isNoTransactionOperation() || info.getTxTransaction().readOnly()) {
                        return txRunningNoTransactionServer;
                    } else {
                        //获取参与方事务执行器
                        return txRunningTransactionServer;
                    }
                }
            } else {
                logger.warn("tx-manager not connected.");
                return txDefaultTransactionServer;
            }
        }
        /*********分布式事务处理逻辑*结束***********/
        logger.debug("分布式事务处理逻辑*结束");
        return txDefaultTransactionServer;
    }
}
