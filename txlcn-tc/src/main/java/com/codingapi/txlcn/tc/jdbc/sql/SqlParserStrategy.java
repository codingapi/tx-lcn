package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.tc.config.TxConfig;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.util.List;

/**
 * @author lorne
 * @date 2020/7/3
 * @description
 */
@AllArgsConstructor
public class SqlParserStrategy {

    private List<SqlParser> sqlParsers;

    private TxConfig txConfig;

    public String parser(Connection connection,String sql){
        SqlParser parser = getSqlParser();
        if(parser!=null){
            return parser.parser(sql,connection);
        }
        return sql;
    }

    private SqlParser getSqlParser(){
        String sqlType = txConfig.getSqlType();
        for(SqlParser parser:sqlParsers){
            if(parser.sqlType().equals(sqlType)){
                return parser;
            }
        }
        return null;
    }
}
