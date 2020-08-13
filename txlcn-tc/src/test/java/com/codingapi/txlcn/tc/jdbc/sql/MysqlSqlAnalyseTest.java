package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.tc.DataSourceConfiguration;
import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.JdbcAnalyseUtils;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import com.codingapi.txlcn.tc.utils.ListUtil;
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
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
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
    public void updateAnalyse() throws SQLException, JSQLParserException {
       String sql = "update lcn_sql_parse_user t1,lcn_sql_parse_user_dept t2 set t1.home_address = 'shanghai',t1.age = 30 where t1.job = t2.dept_name AND t2.dept_name = 'test'";
      //  sql="update lcn_sql_parse_test set dept_name = 3 where dept_name =2";
        Connection connection = dataSource.getConnection();
        String catalog = connection.getCatalog();
        DataBaseContext.getInstance().push(catalog, JdbcAnalyseUtils.analyse(connection));
        TableList tableList =  DataBaseContext.getInstance().get(catalog);

        if(sql.toUpperCase().startsWith("UPDATE")){
            Update statement = (Update) CCJSqlParserUtil.parse(sql);
            if(!checkTableContainsPk(statement,tableList)){
                //TODO
                return;
            }
            String selectSql;
            if(statement.getTables().size() == 1){
                selectSql = updateSqlAnalyseSingleTable(tableList, statement,sql);
            }else {
                selectSql = updateSqlAnalyseMultiTable(tableList, statement,sql);
            }
            QueryRunner queryRunner = new QueryRunner();
            List<Map<String, Object>> query = queryRunner.query(connection, selectSql, new MapListHandler());

           // String setSql = statement.getColumns().stream().map(column -> column.getColumnName().concat(" ")).collect(Collectors.joining(","));
            //String newSql = "update ".concat(updateTableName).concat(" ").concat(updateTableAlias).concat(" set ").concat(setSql).concat(" where ").concat(primaryKey).concat(" in ");

            query.forEach(map->{
            });


        }

    }

    private String updateSqlAnalyseMultiTable(TableList tableList, Update statement, String sql) {
        StringBuilder querySql = new StringBuilder("select ");
        String select = null;
        Set<String> s2 = new TreeSet<>();
        for (Table table : statement.getTables()) {
            TableInfo tableInfo = tableList.getTable(table.getName());
            if(ListUtil.isNotEmpty(tableInfo.getPrimaryKeys())){
                //TODO sql中没有别名的情况
                if(StringUtils.isBlank(select)){
                    select = table.getAlias().getName().concat(".").concat(tableInfo.getPrimaryKeys().get(0));

                }
                String tableName = table.getName().concat(" ").concat(table.getAlias().getName());
                s2.add(tableName);
            }
        }
        String from = s2.stream().collect(Collectors.joining(","));
        querySql.append(select).append(" from ").append(from).append(" where ").append(statement.getWhere().toString());
        log.info("update Sql Analyse =[{}]",querySql.toString());
        return querySql.toString();
    }
    private String updateSqlAnalyseSingleTable(TableList tableList, Update statement,String sql) {
        //单表操作
        Table table = statement.getTables().get(0);
        String querySql = "select ";
        TableInfo tableInfo = tableList.getTable(table.getName());
        String primaryKey = tableInfo.getPrimaryKeys().get(0);
        String aliasName = "";
        if(null != table.getAlias()){
            aliasName = table.getAlias().getName().concat(".");
        }
        querySql = querySql.concat(aliasName).concat((primaryKey).concat(statement.getWhere().toString()));
        log.info("update Sql Analyse =[{}]",querySql);
        return querySql;
    }

    private boolean checkTableContainsPk(Update statement, TableList tableList ) {
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
        String sql = "delete from lcn_sql_parse_user where age = 32";
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
