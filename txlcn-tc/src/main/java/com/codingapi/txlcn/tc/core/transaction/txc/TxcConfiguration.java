package com.codingapi.txlcn.tc.core.transaction.txc;

import org.apache.commons.dbutils.QueryRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Description:
 * Date: 19-1-25 下午3:54
 *
 * @author ujued
 */
@Configuration
public class TxcConfiguration {

    /**
     * Spring ApplicationContext at least one Primary java.sql.DataSource.
     *
     * @param dataSource java.sql.DataSource
     * @return apache jdbc runner
     */
    @Bean
    @Primary
    public QueryRunner queryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }
}
