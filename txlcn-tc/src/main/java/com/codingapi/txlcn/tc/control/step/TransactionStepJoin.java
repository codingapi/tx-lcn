package com.codingapi.txlcn.tc.control.step;

import com.codingapi.txlcn.protocol.await.Lock;
import com.codingapi.txlcn.protocol.await.LockContext;
import com.codingapi.txlcn.protocol.message.event.TransactionCommitEvent;
import com.codingapi.txlcn.protocol.message.event.TransactionJoinEvent;
import com.codingapi.txlcn.tc.config.TxConfig;
import com.codingapi.txlcn.tc.control.TransactionCommitorStrategy;
import com.codingapi.txlcn.tc.control.TransactionState;
import com.codingapi.txlcn.tc.control.TransactionStep;
import com.codingapi.txlcn.tc.exception.TxException;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import com.codingapi.txlcn.tc.jdbc.JdbcTransaction;
import com.codingapi.txlcn.tc.reporter.TxManagerReporter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author lorne
 * @date 2020/3/5
 * @description 加入步骤的业务逻辑
 *
 */
@Slf4j
public class TransactionStepJoin implements TransactionStep {

    private TxManagerReporter managerProtocoler;

    private TransactionCommitorStrategy transactionCommitorStrategy;

    private Executor executor;

    private TxConfig txConfig;

    public TransactionStepJoin(TxManagerReporter managerProtocoler, TransactionCommitorStrategy transactionCommitorStrategy, TxConfig txConfig) {
        this.managerProtocoler = managerProtocoler;
        this.transactionCommitorStrategy = transactionCommitorStrategy;
        this.txConfig = txConfig;
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public TransactionState type() {
        return TransactionState.JOIN;
    }

    @SneakyThrows
    @Override
    public void run(TransactionInfo transactionInfo) {

        // 发送加入事务消息
        long t1 = System.currentTimeMillis();
        TransactionJoinEvent res = (TransactionJoinEvent) managerProtocoler.requestMsg(new TransactionJoinEvent(transactionInfo.getGroupId()));

        if(res==null){
            transactionCommitorStrategy.commit(false);
            throw new TxException("notify transaction fail.");
        }
        long t2 = System.currentTimeMillis();
        log.info("join transaction result:{},time:{}",res.getResult(),(t2-t1));

        //这样要执行groupId等待,等待TM通知事务提交。
        executeJoin(transactionInfo);
    }


    /**
     * 异步等待TM通知
     * @param transactionInfo
     */
    private void executeJoin(TransactionInfo transactionInfo) {
        JdbcTransaction jdbcTransaction =  JdbcTransaction.current();
        executor.execute(()->{
            //重新绑定ThreadLocal信息
            transactionInfo.init();
            jdbcTransaction.init();

            Lock lock = LockContext.getInstance().addKey(transactionInfo.getGroupId());
            if(lock!=null) {
                log.debug("await transaction groupId:{}",transactionInfo.getGroupId());
                lock.await(txConfig.getMaxWaitTransactionTime());
                log.debug("await execute transaction groupId:{}",transactionInfo.getGroupId());
                TransactionCommitEvent event = (TransactionCommitEvent) lock.getRes();
                if (event != null) {
                    transactionCommitorStrategy.commit(event.isCommit());
                } else {
                    //todo 询问TM状态检查
                }
            }

            //清空ThreadLocal信息
            TransactionInfo.clear();
            JdbcTransaction.clear();
        });
    }
}
