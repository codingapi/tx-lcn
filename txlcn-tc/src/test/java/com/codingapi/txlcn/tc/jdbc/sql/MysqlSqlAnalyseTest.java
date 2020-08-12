package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.tc.DataSourceConfiguration;
import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.JdbcAnalyseUtils;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
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
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
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
        String sql = "update lcn_sql_parse_user t1,lcn_sql_parse_user_dept t2 set t1.home_address = 'shanghai',t1.age = 30 where t1.dept_id = t2.id and t2.dept_name = 'test'";
        Connection connection = dataSource.getConnection();
        String catalog = connection.getCatalog();
        DataBaseContext.getInstance().push(catalog, JdbcAnalyseUtils.analyse(connection));
        TableList tableList =  DataBaseContext.getInstance().get(catalog);


        if(sql.toUpperCase().startsWith("UPDATE")){
            Update statement = (Update) CCJSqlParserUtil.parse(sql);
            String updateTableAlias = statement.getColumns().get(0).getTable().getName();
            String updateTableName = statement.getTables().stream().filter(table -> updateTableAlias.equals(table.getAlias().getName())).findAny().get().getName();
            TableInfo table = tableList.getTable(updateTableName);
            List<String> primaryKeys = table.getPrimaryKeys();
            String primaryKey = primaryKeys.get(0);
            StringBuilder querySql = new StringBuilder("select ").append(updateTableAlias).append(".").append(primaryKey).append(" from ");
            String fromTable =  statement.getTables().stream().map(t->t.getName().concat(" ").concat(t.getAlias().getName())).collect(Collectors.joining( ","));
            querySql.append(fromTable).append(" where ");
            String whereSql = statement.getWhere().toString();
            querySql.append(whereSql);
            QueryRunner queryRunner = new QueryRunner();
            List<Map<String, Object>> query = queryRunner.query(connection, querySql.toString(), new MapListHandler());
            String setSql = statement.getColumns().stream().map(column -> column.getColumnName().concat(" ")).collect(Collectors.joining(","));
            String newSql = "update ".concat(updateTableName).concat(" ").concat(updateTableAlias).concat(" set ").concat(setSql).concat(" where ").concat(primaryKey).concat(" in ");
            query.forEach(map->{
                Object o = map.get(primaryKey);
                System.out.println(o);
                newSql.concat((String) o).concat(",");
            });
            System.out.println(newSql);

        }

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
