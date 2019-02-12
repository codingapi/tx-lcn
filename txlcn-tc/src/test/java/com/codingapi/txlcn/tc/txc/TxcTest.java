/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.tc.txc;

import com.codingapi.txlcn.tc.MiniConfiguration;
import com.codingapi.txlcn.tc.TCAutoConfiguration;
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
        queryRunner.update("update t_demo set demo_field=? where id=4", Arrays.copyOf(new Object[]{"aaa", "bbb"}, 1));
    }

    @Test
    public void testDelete() throws SQLException {
        long start = System.currentTimeMillis();
        queryRunner.update("INSERT INTO t_demo (kid, demo_field, group_id, unit_id, create_time, app_name) VALUES ('50777998410975159', 'ujued', '50777347808454984', '3426b848138c45c6bf50b99db3b69439', '2019-01-28 10:20:54.593', 'spring-demo-d:12002')");
        long second = System.currentTimeMillis();
        queryRunner.update("DELETE  FROM t_demo WHERE t_demo.kid=?", "50777998410975159");
        System.out.printf("1: %d, 2: %d", second - start, System.currentTimeMillis() - second);
    }
}
