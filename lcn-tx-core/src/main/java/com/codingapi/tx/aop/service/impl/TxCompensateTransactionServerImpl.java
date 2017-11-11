//package com.lorne.tx.service.impl;
//
//import TxTransactionInfo;
//import TxTransactionLocal;
//import CompensateService;
//import TransactionServer;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.springframework.stereotype.Service;
//
///**
// * Created by yuliang on 2017/7/11.
// */
//@Service(value = "txCompensateTransactionServer")
//public class TxCompensateTransactionServerImpl implements TransactionServer {
//
//    @Override
//    public Object execute(final ProceedingJoinPoint point, TxTransactionInfo info) throws Throwable {
//
//        TxTransactionLocal txTransactionLocal = new TxTransactionLocal();
//        txTransactionLocal.setHasCompensate(true);
//        txTransactionLocal.setGroupId(CompensateService.COMPENSATE_KEY);
//        TxTransactionLocal.setCurrent(txTransactionLocal);
//
//        try {
//            return point.proceed();
//        } catch (Throwable e) {
//            throw e;
//        } finally {
//            TxTransactionLocal.setCurrent(null);
//        }
//
//    }
//}
