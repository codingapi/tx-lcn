package com.codingapi.txlcn.tc;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author lorne
 * @date 2020/8/8
 * @description
 */
@Data
public class LcnTestDataSourceConfig {

    @Value("${lcn.datasource.jdbc-url}")
    private String jdbcUrl;

    @Value("${lcn.datasource.username}")
    private String username;

    @Value("${lcn.datasource.password}")
    private String password;

    @Value("${lcn.datasource.driver-class-name}")
    private String driverClassName;
}
