package com.codingapi.tx.spi.sleuth;

import com.codingapi.tx.spi.sleuth.listener.DefaultSleuthParamListener;
import com.codingapi.tx.spi.sleuth.listener.SleuthParamListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/14
 *
 * @author lorne
 */
@Configuration
@ComponentScan
public class TxSleuthApiConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SleuthParamListener sleuthParamListener(){
        return new DefaultSleuthParamListener();
    }
}