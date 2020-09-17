package com.codingapi.txlcn.tc.jdbc.event;

import com.codingapi.txlcn.tc.constant.TransactionConstant;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import com.codingapi.txlcn.tc.jdbc.JdbcContext;
import com.codingapi.txlcn.tc.jdbc.JdbcTransaction;
import com.codingapi.txlcn.tc.jdbc.TransactionJdbcEvent;
import com.codingapi.txlcn.tc.jdbc.TransactionJdbcState;
import com.codingapi.txlcn.tc.jdbc.log.TransactionLogExecutor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Slf4j
@AllArgsConstructor
public class LcnCommitTransactionJdbcEvent implements TransactionJdbcEvent {

    private TransactionLogExecutor transactionLogExecutor;

    @Override
    public String type() {
        return TransactionConstant.LCN;
    }

    @Override
    public TransactionJdbcState state() {
        return TransactionJdbcState.COMMIT;
    }

    @Override
    public Object execute(Object param) throws SQLException {
        TransactionInfo transactionInfo = TransactionInfo.current();
        String groupId = transactionInfo.getGroupId();
        Connection connection = JdbcTransaction.current().getConnection();
        log.info("commit connection:{}",connection);
        //save sql
        transactionLogExecutor.saveLog();
        //事务提交时需要通过TM控制触发的,这里应该要绑架连接对象，等待TM通知事务提交。
        JdbcContext.getInstance().push(groupId,connection);
        return 1;
    }
}
