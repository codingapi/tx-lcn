package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import com.codingapi.txlcn.tc.utils.ListUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author Gz.
 * @description: 删除语句分析
 * @date 2020-08-13 23:08:26
 */
@Slf4j
public class MysqlSqlDeleteAnalyseStrategy implements SqlSqlAnalyseHandler {

    @Override
    public String mysqlAnalyseStrategy(String sql, Connection connection,Statement stmt) throws SQLException, JSQLParserException {
        String catalog = connection.getCatalog();
        /**
         * todo JdbcAnalyseUtils.analyse(connection) 不能在此使用
         * JdbcAnalyseUtils.analyse(connection) 是系统启动的时候获取数据的，而非在执行sql的时候做数据处理 {@link com.codingapi.txlcn.tc.aspect.TxDataSourceInterceptor#invoke(MethodInvocation)}
         */
        TableList tableList =  DataBaseContext.getInstance().get(catalog);
        Delete statement = (Delete) stmt;
        Table table = statement.getTable();
        if(!SqlAnalyseHelper.checkTableContainsPk(table, tableList)){
            return sql;
        }
        if(SqlAnalyseHelper.checkWhereContainsPk(table, tableList,statement.getWhere().toString())){
            return sql;
        }
        SqlAnalyseInfo sqlAnalyseInfo = SqlAnalyseHelper.sqlAnalyseSingleTable(tableList, table, statement.getWhere(),statement.getJoins());
        QueryRunner queryRunner = new QueryRunner();
        List<Map<String, Object>> query = queryRunner.query(connection, sqlAnalyseInfo.getQuerySql(), new MapListHandler());
        if(ListUtil.isEmpty(query)){
            return sql;
        }
        sql = SqlAnalyseHelper.getNewSql(sql, sqlAnalyseInfo, query);
        log.info("newSql=[{}]",sql);
        return sql;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        AnalyseStrategryFactory.register(MysqlAnalyseEnum.DELETE.name(), this);
    }


}
