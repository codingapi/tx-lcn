package com.codingapi.txlcn.tc.jdbc.event;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import com.codingapi.txlcn.tc.constant.TransactionConstant;
import com.codingapi.txlcn.tc.jdbc.TransactionJdbcEvent;
import com.codingapi.txlcn.tc.jdbc.TransactionJdbcState;
import com.codingapi.txlcn.tc.jdbc.sql.SqlAnalyseStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Slf4j
@AllArgsConstructor
public class LcnAfterTransactionJdbcEvent implements TransactionJdbcEvent {

    private SqlAnalyseStrategy sqlAnalyseStrategy;

    @Override
    public String type() {
        return TransactionConstant.LCN;
    }

    @Override
    public TransactionJdbcState state() {
        return TransactionJdbcState.AFTER;
    }

    @Override
    public Object execute(Object param) throws SQLException {
        StatementInformation statementInformation = (StatementInformation) param;
        String sql = statementInformation.getSqlWithValues();
        log.info("sql=>{}",sql);
        //这里要分析sql获取，真实变动的数据.不需要获取之前的数据
        sql = sqlAnalyseStrategy.analyse(sql,statementInformation);
        return sql;
    }
}
