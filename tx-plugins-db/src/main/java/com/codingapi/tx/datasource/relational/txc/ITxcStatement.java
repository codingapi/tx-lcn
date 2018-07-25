package com.codingapi.tx.datasource.relational.txc;

import java.sql.Statement;

/**
 * [类描述]
 *
 * @author caican
 * @date 17/12/4
 */
public interface ITxcStatement extends Statement {
    /** 返回执行的SQL
     * @return SQL语句
     */
    String getSql();

    Statement getStatement();

    AbstractTxcConnection getTxcDBConnection();

}
