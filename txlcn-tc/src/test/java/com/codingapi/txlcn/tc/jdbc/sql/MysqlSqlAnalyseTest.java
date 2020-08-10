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
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
}
