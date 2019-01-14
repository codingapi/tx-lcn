package com.codingapi.txlcn.client;

import com.codingapi.txlcn.client.core.txc.resource.init.DefaultTxcSettingFactory;
import com.codingapi.txlcn.client.core.txc.resource.init.TxcSettingFactory;
import com.codingapi.txlcn.client.support.checking.DTXChecking;
import com.codingapi.txlcn.client.support.checking.SimpleDTXChecking;
import com.codingapi.txlcn.client.support.common.template.TransactionCleanTemplate;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author lorne
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties
public class TxClientConfiguration {


    @Bean
    public QueryRunner queryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public TxcSettingFactory txcSettingFactory() {
        return new DefaultTxcSettingFactory();
    }


    @Bean
    public SmartInitializingSingleton dtxCheckingTransactionCleanTemplateAdapter(DTXChecking dtxChecking,
                                                                                 TransactionCleanTemplate transactionCleanTemplate) {
        if (dtxChecking instanceof SimpleDTXChecking) {
            return () -> ((SimpleDTXChecking) dtxChecking).setTransactionCleanTemplate(transactionCleanTemplate);
        }
        return () -> {
        };
    }
}
