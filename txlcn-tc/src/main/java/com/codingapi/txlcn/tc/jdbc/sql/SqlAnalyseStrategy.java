package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import com.codingapi.txlcn.tc.config.TxConfig;
import lombok.AllArgsConstructor;

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

    public String analyse(String sql,StatementInformation statementInformation){
        SqlAnalyse parser = getSqlAnalyse();
        if(parser!=null){
            return parser.analyse(sql,statementInformation);
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
