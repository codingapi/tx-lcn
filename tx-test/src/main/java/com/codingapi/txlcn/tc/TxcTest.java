package com.codingapi.txlcn.tc;

import org.apache.commons.dbutils.QueryRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * Description:
 * Date: 19-1-26 下午5:09
 *
 * @author ujued
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MiniConfiguration.class, TCAutoConfiguration.class})
public class TxcTest {

    @Autowired
    private QueryRunner queryRunner;

    @Test
    public void testQueryRunner() throws SQLException {
        queryRunner.update("update t_demo set demo_field=? where id=4", Arrays.copyOf(new Object[]{"aaa","bbb"}, 1));
    }
}
