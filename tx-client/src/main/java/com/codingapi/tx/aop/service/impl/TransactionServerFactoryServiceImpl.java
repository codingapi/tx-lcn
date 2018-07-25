package com.codingapi.tx.aop.service.impl;


import com.codingapi.tx.aop.bean.TxTransactionInfo;
import com.codingapi.tx.aop.service.TransactionServer;
import com.codingapi.tx.aop.service.TransactionServerFactoryService;
import com.codingapi.tx.datasource.ILCNTransactionControl;
import com.codingapi.tx.framework.utils.SocketManager;
import com.codingapi.tx.netty.service.NettyService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * Created by lorne on 2017/6/8.
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
    private NettyService nettyService;

    @Autowired
    private ILCNTransactionControl transactionControl;


    public TransactionServer createTransactionServer(TxTransactionInfo info) throws Throwable {

        if (!SocketManager.getInstance().isNetState()) {
            logger.warn("tx-manager not connected.");
            return txDefaultTransactionServer;
        }

        /*********分布式事务处理逻辑*开始***********/

        /** 尽当Transaction注解不为空，其他都为空时。表示分布式事务开始启动 **/
        if (info.getTransaction() != null && info.getTransaction().isStart() && info.getTxTransactionLocal() == null && StringUtils.isEmpty(info.getTxGroupId())) {
            //检查socket通讯是否正常 （当启动事务的主业务方法执行完以后，再执行其他业务方法时将进入txInServiceTransactionServer业务处理）
            if (SocketManager.getInstance().isNetState()) {
                return txStartTransactionServer;
            } else {
                logger.warn("tx-manager not connected.");
                return txDefaultTransactionServer;
            }
        }


        /** 分布式事务已经开启，业务进行中 **/
        if (info.getTxTransactionLocal() != null || StringUtils.isNotEmpty(info.getTxGroupId())) {
            //检查socket通讯是否正常 （第一次执行时启动txRunningTransactionServer的业务处理控制，然后嵌套调用其他事务的业务方法时都并到txInServiceTransactionServer业务处理下）
            if (SocketManager.getInstance().isNetState()) {
                if (info.getTxTransactionLocal() != null) {
                    return txDefaultTransactionServer;
                } else {
                    if(transactionControl.isNoTransactionOperation() // 表示整个应用没有获取过DB连接
                        || info.getTransaction().readOnly()) { //无事务业务的操作
                        return txRunningNoTransactionServer;
                    }else {
                        return txRunningTransactionServer;
                    }
                }
            } else {
                logger.warn("tx-manager not connected.");
                return txDefaultTransactionServer;
            }
        }
        /*********分布式事务处理逻辑*结束***********/

        return txDefaultTransactionServer;
    }
}
