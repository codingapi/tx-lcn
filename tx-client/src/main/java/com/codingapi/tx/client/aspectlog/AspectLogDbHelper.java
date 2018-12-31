package com.codingapi.tx.client.aspectlog;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

/**
 * Description: h2数据库操作类
 * Company: CodingApi
 * Date: 2018/12/20
 *
 * @author codingapi
 */
@Slf4j
public class AspectLogDbHelper implements DisposableBean {

    private HikariDataSource hikariDataSource;

    private QueryRunner queryRunner;

    @Autowired
    private AspectLogDbProperties aspectLogDbProperties;

    @PostConstruct
    public void init() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(org.h2.Driver.class.getName());
        hikariConfig.setJdbcUrl(String.format("jdbc:h2:%s",aspectLogDbProperties.getFilePath()));
        hikariDataSource = new HikariDataSource(hikariConfig);
        queryRunner = new QueryRunner(hikariDataSource);
        log.info("init db finish.");
    }


    public int update(String sql, Object... params) {
        try {
            return queryRunner.update(sql, params);
        } catch (SQLException e) {
            log.error("update error",e);
            return 0;
        }
    }


    public <T> T query(String sql, ResultSetHandler<T> rsh,Object ... params){
        try {
            return queryRunner.query(sql, rsh,params);
        } catch (SQLException e) {
            log.error("query error",e);
            return null;
        }
    }

    public <T> T query(String sql,ScalarHandler<T> scalarHandler,Object ... params){
        try {
            return queryRunner.query(sql,scalarHandler,params);
        } catch (SQLException e) {
            log.error("query error",e);
            return null;
        }
    }

    @Override
    public void destroy() throws Exception {
        hikariDataSource.close();
        log.info("log hikariDataSource close.");
    }
}
