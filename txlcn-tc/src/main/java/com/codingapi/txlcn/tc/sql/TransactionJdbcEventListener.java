package com.codingapi.txlcn.tc.sql;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import com.codingapi.txlcn.p6spy.event.P6spyJdbcEventListener;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Slf4j
public class TransactionJdbcEventListener extends P6spyJdbcEventListener {

    @Override
    public String onBeforeAnyExecute(StatementInformation statementInformation) throws SQLException {
        log.info("sql=>{}",statementInformation.getSqlWithValues());
        return super.onBeforeAnyExecute(statementInformation);
    }
}
