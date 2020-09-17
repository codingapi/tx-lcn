package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.tc.DataSourceConfiguration;
import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.JdbcAnalyseUtils;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import org.apache.commons.dbutils.DbUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * @author lorne
 * @date 2020/8/8
 * @description
 */
@SpringBootTest(classes = DataSourceConfiguration.class)
public class DataBaseAnalyseTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void analyse() throws SQLException {

        Connection connection = dataSource.getConnection();

        List<TableInfo> tableInfos = JdbcAnalyseUtils.analyse(connection);

        String catalog = connection.getCatalog();
        DataBaseContext.getInstance().push(catalog,tableInfos);

        DbUtils.close(connection);

        TableList tableList =  DataBaseContext.getInstance().get(catalog);

        Assert.isTrue(!tableList.isEmpty(),"加载数据异常.");
    }
}
