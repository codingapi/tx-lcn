package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.tc.DataSourceConfiguration;
import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.JdbcAnalyseUtils;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.MysqlAnalyseContextEnum;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.insert.Insert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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
    public void mysqlAnalyse() throws SQLException, JSQLParserException {
        String sql = "DELETE  t2,t3 FROM lcn_sql_parse_test2 t2 ,lcn_sql_parse_test3 t3 where t3.job = t2.dept_name AND t2.dept_name = 'test' and t3.name = 'a' ";
        sql = "update lcn_sql_parse_test3 t3 ,lcn_sql_parse_test2 t2 set t3.age = 56 ,t2.dept_name = 'dev' where t3.job = t2.dept_name and t2.dept_name = 'test'";
        Connection connection = dataSource.getConnection();
        MysqlAnalyseContextEnum.valueOf(sql.toUpperCase().substring(0,6)).executeStrategry(sql,connection);
    }

    @Test
    public void mysqlAnalyseInsert() throws SQLException, JSQLParserException {
        String sql = "insert into lcn_sql_parse_test2 (dept_name) values ('a')";
        sql = "insert into lcn_sql_parse_test1 (id, name, sex, job, home_address, age, dept_id) values (null,'gz','1','test','bjc',12,1)";
        sql = "INSERT INTO lcn_sql_parse_test1 (name, sex, job, home_address, age, dept_id) VALUES ('gz', '1', 'test', 'bjc', 12, 4),('gz', '1', 'test', 'bjc', 12, 5),('gz', '1', 'test', 'bjc', 12, 6)";
        Connection connection = dataSource.getConnection();
        MysqlAnalyseContextEnum.valueOf(sql.toUpperCase().substring(0,6)).executeStrategry(sql,connection);
    }
}
