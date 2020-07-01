package com.codingapi.txlcn.tc.jdbc;

import com.codingapi.txlcn.p6spy.common.ConnectionInformation;
import com.codingapi.txlcn.p6spy.common.StatementInformation;
import com.codingapi.txlcn.p6spy.event.JdbcCallable;
import com.codingapi.txlcn.p6spy.event.P6spyJdbcEventListener;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Slf4j
public class TransactionJdbcEventListener extends P6spyJdbcEventListener {

    private List<TransactionJdbcEvent> transactionJdbcEvents;

    public TransactionJdbcEventListener(List<TransactionJdbcEvent> transactionJdbcEvents) {
        this.transactionJdbcEvents = transactionJdbcEvents;
        if(this.transactionJdbcEvents==null){
            this.transactionJdbcEvents=new ArrayList<>();
        }
    }

    private Optional<TransactionJdbcEvent> getTransactionJdbcEvent(TransactionJdbcState state){
        TransactionInfo transactionInfo = TransactionInfo.current();
        for(TransactionJdbcEvent event:transactionJdbcEvents){
            if(transactionInfo.getTransactionType().equals(event.type())&&state.equals(event.state())){
                return Optional.ofNullable(event);
            }
        }
        return Optional.empty();
    }

    @Override
    public String onBeforeAnyExecute(StatementInformation statementInformation) throws SQLException {
        String sql = statementInformation.getSqlWithValues();
        Optional<TransactionJdbcEvent> optional =  getTransactionJdbcEvent(TransactionJdbcState.EXECUTE);
        if(!optional.isPresent()){
            return sql;
        }
        return (String)optional.get().execute(sql);
    }

    @Override
    public void onBeforeCommit(ConnectionInformation connectionInformation, JdbcCallable callable) throws SQLException{
        Optional<TransactionJdbcEvent> optional =  getTransactionJdbcEvent(TransactionJdbcState.COMMIT);
        if(!optional.isPresent()){
            callable.call();
        }
        optional.get().execute(callable);
    }

    @Override
    public void onBeforeRollback(ConnectionInformation connectionInformation, JdbcCallable callable) throws SQLException {
        Optional<TransactionJdbcEvent> optional =  getTransactionJdbcEvent(TransactionJdbcState.ROLLBACK);
        if(!optional.isPresent()){
            callable.call();
        }
        optional.get().execute(callable);
    }

    @SneakyThrows
    @Override
    public void onAfterAnyExecute(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        Optional<TransactionJdbcEvent> optional =  getTransactionJdbcEvent(TransactionJdbcState.AFTER);
        if(optional.isPresent()){
            optional.get().execute(statementInformation);
        }
    }
}
