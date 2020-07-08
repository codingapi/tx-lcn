package com.codingapi.txlcn.tc.control.step;

import com.codingapi.txlcn.protocol.message.event.TransactionNotifyEvent;
import com.codingapi.txlcn.protocol.message.event.TransactionNotifyReplyEvent;
import com.codingapi.txlcn.tc.config.TxConfig;
import com.codingapi.txlcn.tc.control.TransactionCommitorStrategy;
import com.codingapi.txlcn.tc.control.TransactionState;
import com.codingapi.txlcn.tc.control.TransactionStep;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import com.codingapi.txlcn.tc.jdbc.JdbcTransaction;
import com.codingapi.txlcn.tc.reporter.TxManagerReporter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

/**
 * @author lorne
 * @date 2020/3/5
 * @description 通知步骤的业务逻辑
 *
 */
@Slf4j
@AllArgsConstructor
public class TransactionStepNotify implements TransactionStep {

    private final TxManagerReporter managerProtocoler;

    private final TransactionCommitorStrategy transactionCommitorStrategy;

    private final TxConfig txConfig;

    @Override
    public TransactionState type() {
        return TransactionState.NOTIFY;
    }

    @Override
    public void run(TransactionInfo transactionInfo) {
        long t1 = System.currentTimeMillis();
        //事务组ID
        String groupId = transactionInfo.getGroupId();
        //本地事务执行结果
        Boolean successReturn = transactionInfo.getSuccessReturn();
        //应用名
        String applicationName = txConfig.getApplicationName();
        //获取到connection
        Connection connection = JdbcTransaction.current().getConnection();

        TransactionNotifyEvent res = (TransactionNotifyEvent) managerProtocoler.requestMsg(new TransactionNotifyEvent(groupId,successReturn,applicationName));

        while (transactionInfo.getSuccessReturn()&&res==null){
            //尝试再次通知.
            res = (TransactionNotifyEvent) managerProtocoler.requestMsg(new TransactionNotifyEvent(groupId,successReturn,applicationName));
        }

        //是否需要提交事务
        boolean needCommit = res != null && (res.isSuccess() && transactionInfo.getSuccessReturn());
        //当TM返回是正常时,则需要提交本地事务.
        transactionCommitorStrategy.commit(needCommit);

        //tm响应不为空
        if(res!=null){
            //收到TM响应 主动给TM发送通知回复事件
            managerProtocoler.sendMsg(new TransactionNotifyReplyEvent(groupId,needCommit,applicationName));
        }

        long t2 = System.currentTimeMillis();
        log.info("notify transaction result:{},time:{}",res,(t2-t1));


    }
}
