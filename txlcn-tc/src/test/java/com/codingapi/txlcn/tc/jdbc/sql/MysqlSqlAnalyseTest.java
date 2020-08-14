package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.tc.DataSourceConfiguration;
import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.JdbcAnalyseUtils;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.MysqlUpdateAnalyseStrategry;
import com.codingapi.txlcn.tc.utils.ListUtil;
import com.google.common.collect.Maps;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.JSqlParser;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.dbutils.BaseResultSetHandler;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lorne
 * @date 2020/8/8
 * @description
 */
@SpringBootTest(classes = DataSourceConfiguration.class)
@Slf4j
public class MysqlSqlAnalyseTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void analyse() throws SQLException, JSQLParserException {
        String sql = "insert into lcn_demo(name,module) values('123','tc-c')";
        Connection connection =  dataSource.getConnection();
        String catalog = connection.getCatalog();
        DataBaseContext.getInstance().push(catalog, JdbcAnalyseUtils.analyse(connection));

        QueryRunner queryRunner = new QueryRunner();
        int res =  queryRunner.execute(connection,sql);

        Insert insert = (Insert) CCJSqlParserUtil.parse(sql);
        String tableName = insert.getTable().getName();
        TableList tableList =  DataBaseContext.getInstance().get(catalog);
        TableInfo tableInfo =  tableList.getTable(tableName);
        log.info("tableInfo:{}",tableInfo);

        assertTrue(res>0,"数据插入异常.");
        DbUtils.close(connection);
    }

    @Test
    public void updateAnalyse() throws SQLException, JSQLParserException, UnsupportedEncodingException {
       String sql = "update lcn_sql_parse_test1 t1,lcn_sql_parse_test2 t2  set t1.home_address = 'shanghai',t1.age = 30 where t1.job = t2.dept_name AND t2.dept_name = 'test'";
      //  sql="update lcn_sql_parse_test set dept_name = 3 where dept_name =2";
        sql = "update lcn_sql_parse_test3 t1,lcn_sql_parse_test2 t2  set t1.home_address = 'shanghai',t1.age = 30 where t1.job = t2.dept_name AND t2.dept_name = 'test'";
        Connection connection = dataSource.getConnection();
        String catalog = connection.getCatalog();
        DataBaseContext.getInstance().push(catalog, JdbcAnalyseUtils.analyse(connection));
        TableList tableList =  DataBaseContext.getInstance().get(catalog);
        Update statement = (Update) CCJSqlParserUtil.parse(sql);
        if(!checkTableContainsPk(statement,tableList)){
            //TODO
            return;
        }
        String selectSql;
        Map<String,String> sqlMap = Maps.newHashMap();
        if(statement.getTables().size() == 1){
            sqlMap = updateSqlAnalyseSingleTable(tableList, statement,sql);
        }else {
            sqlMap = updateSqlAnalyseMultiTable(tableList, statement,sql);
        }
        String select = sqlMap.get("select");
        String querySql = sqlMap.get("querySql");
        String primaryKey = sqlMap.get("primaryKey");
        QueryRunner queryRunner = new QueryRunner();
        List<Map<String, Object>> query = queryRunner.query(connection, querySql, new MapListHandler());
        sql = sql.concat(" and ").concat(select).concat(" in ( ");

        int size = query.size();
        for (int i = 0; i < query.size(); i++) {
            String str = new String((byte[]) query.get(i).get(primaryKey), "utf-8");

            sql =  sql.concat(str);
            if(i + 1 < size){
                sql = sql.concat(" , ");
            }
        }
        sql = sql.concat(")");

        System.out.println(sql);
    }

    private  Map<String,String> updateSqlAnalyseMultiTable(TableList tableList, Update statement, String sql) {
        Map<String,String> sqlMap = Maps.newHashMap();
        StringBuilder querySql = new StringBuilder("select ");
        String select = null;
        Set<String> s2 = new TreeSet<>();
        for (Table table : statement.getTables()) {
            TableInfo tableInfo = tableList.getTable(table.getName());
            if(ListUtil.isNotEmpty(tableInfo.getPrimaryKeys())){
                //TODO sql中没有别名的情况
                if(StringUtils.isBlank(select)){
                    select = table.getAlias().getName().concat(".").concat(tableInfo.getPrimaryKeys().get(0));
                    sqlMap.put("primaryKey",tableInfo.getPrimaryKeys().get(0));

                }
                String tableName = table.getName().concat(" ").concat(table.getAlias().getName());
                s2.add(tableName);
            }
        }
        String from = s2.stream().collect(Collectors.joining(","));
        querySql.append(select).append(" from ").append(from).append(" where ").append(statement.getWhere().toString());
        log.info("update Sql Analyse =[{}]",querySql.toString());
        sqlMap.put("select",select);
        sqlMap.put("querySql",querySql.toString());
        return sqlMap;
    }


    private Map<String,String> updateSqlAnalyseSingleTable(TableList tableList, Update statement,String sql) {
        Map<String,String> sqlMap = Maps.newHashMap();
        //单表操作
        Table table = statement.getTables().get(0);
        String querySql = "select ";
        TableInfo tableInfo = tableList.getTable(table.getName());
        String primaryKey = tableInfo.getPrimaryKeys().get(0);
        String select = "";
        if(null != table.getAlias()){
            select = table.getAlias().getName().concat(".").concat(primaryKey);
        }
        querySql = querySql.concat(select).concat(statement.getWhere().toString());
        log.info("update Sql Analyse =[{}]",querySql);
        sqlMap.put("primaryKey",primaryKey);
        sqlMap.put("select",select);
        sqlMap.put("querySql",querySql);
        return sqlMap;
    }

    private  boolean checkTableContainsPk(Update statement, TableList tableList ) {
        boolean containsPk = false;
        for (Table table : statement.getTables()) {
            TableInfo tableInfo = tableList.getTable(table.getName());
            if(ListUtil.isNotEmpty(tableInfo.getPrimaryKeys())){
                containsPk =true;
            }
        }
        return containsPk;
    }


    /**
     * 删除语句分析测试样例
     *
     * @throws SQLException
     * @throws JSQLParserException
     */
    @Test
    public void deleteAnalyse() throws SQLException, JSQLParserException {
        String sql = "delete from lcn_sql_parse_test1 where age = 32";
        Connection connection = dataSource.getConnection();
        String catalog = connection.getCatalog();
        DataBaseContext.getInstance().push(catalog, JdbcAnalyseUtils.analyse(connection));

        TableList tableList =  DataBaseContext.getInstance().get(catalog);

        if(sql.toUpperCase().startsWith("DELETE")){
            Delete delete = (Delete) CCJSqlParserUtil.parse(sql);
            String name = delete.getTable().getName();
            TableInfo table = tableList.getTable(name);
            List<String> primaryKeys = table.getPrimaryKeys();

            String primaryKey = primaryKeys.get(0);
            String querySql = sql.replace(sql.substring(0,6), "select ".concat(primaryKey));
            QueryRunner queryRunner = new QueryRunner();
            List<Map<String, Object>> query = queryRunner.query(connection, querySql, new MapListHandler());
            StringBuilder sb = new StringBuilder("delete from").append(" ").append(name).append(" ").append("where").append(" ").append(primaryKey).append(" ").append("in").append(" ").append("(").append(" ");
            query.forEach(map->{
                Object o = map.get(primaryKey);
                sb.append(o).append(" ").append(",");
            });
            String newSql = sb.replace(sb.lastIndexOf(","),sb.length(),")").toString();
            System.out.println(newSql);
        }


//        TableInfo tableInfo =  tableList.getTable(tableName);
//        log.info("tableInfo:{}",tableInfo);
//
//        assertTrue(res>0,"数据插入异常.");
//        DbUtils.close(connection);
    }
}
