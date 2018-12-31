package com.codingapi.tx.client;

import com.codingapi.tx.client.spi.transaction.txc.resource.sql.init.DefaultTxcSettingFactory;
import com.codingapi.tx.client.spi.transaction.txc.resource.sql.init.TxcSettingFactory;
import com.codingapi.tx.commons.util.serializer.ProtostuffSerializer;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
@ComponentScan
public class TxClientConfiguration {

    @Bean
    public ProtostuffSerializer protostuffSerializer() {
        return new ProtostuffSerializer();
    }

    @Bean
    public QueryRunner queryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public TxcSettingFactory txcSettingFactory() {
        return new DefaultTxcSettingFactory();
    }
}
