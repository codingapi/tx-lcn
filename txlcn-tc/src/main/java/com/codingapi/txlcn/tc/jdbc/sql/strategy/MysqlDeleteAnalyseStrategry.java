package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.delete.Delete;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Slf4j
public class MysqlDeleteAnalyseStrategry implements MysqlSqlAnalyseStrategry {

    @Override
    public String MysqlAnalyseStrategry(String sql, StatementInformation statementInformation) throws SQLException, JSQLParserException {
        Connection connection =  statementInformation.getConnectionInformation().getConnection();
        TableList tableList = DataBaseContext.getInstance().get(connection);
        Delete delete = (Delete) CCJSqlParserUtil.parse(sql);
        String name = delete.getTable().getName();
        TableInfo table = tableList.getTable(name);
        List<String> primaryKeys = table.getPrimaryKeys();

        String primaryKey = primaryKeys.get(0);
        String querySql = sql.replace(sql.substring(0,6), "select ".concat(primaryKey));
        QueryRunner queryRunner = new QueryRunner();
        List<Map<String, Object>> queryList = queryRunner.query(connection, querySql, new MapListHandler());
        StringBuilder sb = new StringBuilder("delete from").append(" ").append(name).append(" ").append("where").append(" ").append(primaryKey).append(" ");
        if(queryList == null || queryList.size() == 0){
            //主键非空且唯一 等于不做删除.
            sb.append(" is null").append(" )");
            return sb.toString();
        }
       sb.append("in").append(" ").append("(").append(" ");
        queryList.forEach(map->{
            Object o = map.get(primaryKey);
            sb.append(o).append(" ").append(",");
        });
        sql = sb.replace(sb.lastIndexOf(","),sb.length(),")").toString();
        log.info("new sql=[{}]",sql);

        return null;
    }
}
