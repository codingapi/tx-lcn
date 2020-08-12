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
        String sql = "update lcn_sql_parse_demo set home_address = 'beijing' where job = 'test'";
        Connection connection = dataSource.getConnection();
        String catalog = connection.getCatalog();
        DataBaseContext.getInstance().push(catalog, JdbcAnalyseUtils.analyse(connection));

        QueryRunner queryRunner = new QueryRunner();
        int res = queryRunner.execute(connection, sql);
        TableList tableList =  DataBaseContext.getInstance().get(catalog);


        if(sql.toUpperCase().startsWith("UPDATE")){
            Update statement = (Update) CCJSqlParserUtil.parse(sql);
            List<Table> tables = statement.getTables();
            tables.forEach(table -> {
                System.out.println(table);
                TableInfo tableInfo =  tableList.getTable(table.getName());

            });
        }

    }


    /**
     * 删除语句分析测试样例
     * DROP TABLE IF EXISTS `lcn_sql_parse_demo`;
     * CREATE TABLE `lcn_sql_parse_demo` (
     *   `id` int(12) NOT NULL AUTO_INCREMENT,
     *   `name` varchar(30) DEFAULT NULL comment '姓名',
     *   `sex` varchar(10)  DEFAULT NULL comment '性别',
     *   `job` varchar(32) DEFAULT NULL comment '工作',
     *   `home_address` varchar(128) default null comment '家庭住址',
     *   `age` int default null comment '年龄',
     *   PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
     *
     * SET FOREIGN_KEY_CHECKS = 1;
     *
     * @throws SQLException
     * @throws JSQLParserException
     */
    @Test
    public void deleteAnalyse() throws SQLException, JSQLParserException {
        String sql = "delete from lcn_sql_parse_demo where age = 32";
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
