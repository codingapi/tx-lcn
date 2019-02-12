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
package com.codingapi.txlcn.tc.corelog;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * Description: h2数据库操作类
 * Company: CodingApi
 * Date: 2018/12/20
 *
 * @author codingapi
 */
@Slf4j
@Component
public class H2DbHelper implements DisposableBean {

    private final HikariDataSource hikariDataSource;

    private final QueryRunner queryRunner;

    @Autowired
    public H2DbHelper(H2DbProperties h2DbProperties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(org.h2.Driver.class.getName());
        hikariConfig.setJdbcUrl(String.format("jdbc:h2:%s", h2DbProperties.getFilePath()));
        hikariDataSource = new HikariDataSource(hikariConfig);
        queryRunner = new QueryRunner(hikariDataSource);
        log.info("Init H2 DATABASE at {}", h2DbProperties.getFilePath());
    }

    public QueryRunner queryRunner() {
        return queryRunner;
    }

    public int update(String sql, Object... params) {
        try {
            return queryRunner.update(sql, params);
        } catch (SQLException e) {
            log.error("update error", e);
            return 0;
        }
    }


    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) {
        try {
            return queryRunner.query(sql, rsh, params);
        } catch (SQLException e) {
            log.error("query error", e);
            return null;
        }
    }

    public <T> T query(String sql, ScalarHandler<T> scalarHandler, Object... params) {
        try {
            return queryRunner.query(sql, scalarHandler, params);
        } catch (SQLException e) {
            log.error("query error", e);
            return null;
        }
    }

    @Override
    public void destroy() throws Exception {
        hikariDataSource.close();
        log.info("log hikariDataSource close.");
    }
}
