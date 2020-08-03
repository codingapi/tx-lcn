package com.codingapi.example.tc.service;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoServiceTest {

    @Autowired
    private DataSource dataSource;

    private QueryRunner queryRunner = new QueryRunner();

    @SneakyThrows
    @Test
    void save() {
        Connection connection = dataSource.getConnection();
        String sql ="update lcn_demo set name = ? where id = ? ";
        int res = queryRunner.execute(connection,sql,new Object[]{"b",1});
        System.out.println(res);
    }
}