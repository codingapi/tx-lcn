package com.codingapi.tx.aop.service.impl;

//import TxTransaction;
import com.codingapi.tx.aop.bean.TxTransactionInfo;
import com.codingapi.tx.netty.service.NettyService;
//import TxTransactionLocal;
//import CompensateService;
//import CompensateServiceImpl;
import com.codingapi.tx.datasource.ILCNDataSourceProxy;
import com.codingapi.tx.aop.service.TransactionServer;
import com.codingapi.tx.aop.service.TransactionServerFactoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by lorne on 2017/6/8.
 */
@Service
public class TransactionServerFactoryServiceImpl implements TransactionServerFactoryService {


    @Autowired
    private TransactionServer txStartTransactionServer;

    @Autowired
    private TransactionServer txRunningTransactionServer;

    @Autowired
    private TransactionServer txDefaultTransactionServer;

//    @Autowired
//    private TransactionServer txCompensateTransactionServer;

    @Autowired
    private TransactionServer txRunningNoTransactionServer;

    @Autowired
    private NettyService nettyService;

    @Autowired
    private ILCNDataSourceProxy baseProxy;


    public TransactionServer createTransactionServer(TxTransactionInfo info) throws Throwable {


//        /*********补偿事务处理逻辑*开始***********/
//        /** 事务补偿业务处理中**/
//        if (CompensateService.COMPENSATE_KEY.equals(info.getTxGroupId())) {
//            //控制返回业务数据，但让其事务回滚。第一次执行时，需要启用线程控制事务，后面的事务与开始启动的事务事务嵌套即可。然后通过开始事务统一回滚。
//            //因此执行业务过程中时的事务与txInServiceTransactionServer处理一致
//            if (TxTransactionLocal.current() != null) {
//                return txDefaultTransactionServer;
//            } else {
//                return txCompensateTransactionServer;
//            }
//        }
//
//        /** 事务补偿业务开始标示**/
//        if (info.getCompensate() != null) {
//            //正常处理，同模下将依旧执行方法。
//            return txCompensateTransactionServer;
//        }
//
//        /*********补偿事务处理逻辑*结束***********/
//
//
//        if (CompensateServiceImpl.hasCompensate) {
//            //事务补偿未执行完毕
//            throw new Exception("事务补偿运行中,请稍后再访问.");
//        }


        /*********分布式事务处理逻辑*开始***********/

        /** 尽当Transaction注解不为空，其他都为空时。表示分布式事务开始启动 **/
        if (info.getTransaction() != null && info.getTxTransactionLocal() == null && StringUtils.isEmpty(info.getTxGroupId())) {
            //检查socket通讯是否正常 （当启动事务的主业务方法执行完以后，再执行其他业务方法时将进入txInServiceTransactionServer业务处理）
            if (nettyService.checkState()) {
                return txStartTransactionServer;
            } else {
                throw new Exception("tx-manager尚未链接成功,请检测tx-manager服务");
            }
        }


        /** 分布式事务已经开启，业务进行中 **/
        if (info.getTxTransactionLocal() != null || StringUtils.isNotEmpty(info.getTxGroupId())) {
            //检查socket通讯是否正常 （第一次执行时启动txRunningTransactionServer的业务处理控制，然后嵌套调用其他事务的业务方法时都并到txInServiceTransactionServer业务处理下）
            if (nettyService.checkState()) {
                if (info.getTxTransactionLocal() != null) {
                    return txDefaultTransactionServer;
                } else {
                    if(baseProxy.hasTransaction()) {//有事务业务的操作
                        return txRunningTransactionServer;
                    }else {
                        return txRunningNoTransactionServer;
                    }
                }
            } else {
                throw new Exception("tx-manager尚未链接成功,请检测tx-manager服务");
            }
        }
        /*********分布式事务处理逻辑*结束***********/

        return txDefaultTransactionServer;
    }
}
