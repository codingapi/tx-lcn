package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import com.codingapi.txlcn.tc.config.TxConfig;
import com.codingapi.txlcn.tc.jdbc.JdbcTransaction;
import com.codingapi.txlcn.tc.jdbc.log.TransactionLog;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.List;

/**
 * @author lorne
 * @date 2020/7/3
 * @description
 */
@AllArgsConstructor
public class SqlAnalyseStrategy {

    private List<SqlAnalyse> sqlAnalyses;

    private TxConfig txConfig;

    public String analyse(String sql,StatementInformation statementInformation) throws SQLException {
        SqlAnalyse analyse = getSqlAnalyse();
        if(analyse!=null){
            //是否需要分析
            if(analyse.preAnalyse(sql)) {
                //sql分析处理
                String newSql =  analyse.analyse(sql, statementInformation);
                //添加到事务日志中.
                TransactionLog transactionLog = new TransactionLog(newSql);
                JdbcTransaction.current().add(transactionLog);
                return newSql;
            }
        }
        return sql;
    }

    private SqlAnalyse getSqlAnalyse(){
        String sqlType = txConfig.getSqlType();
        for(SqlAnalyse analyse: sqlAnalyses){
            if(analyse.sqlType().equals(sqlType)){
                return analyse;
            }
        }
        return null;
    }

}
