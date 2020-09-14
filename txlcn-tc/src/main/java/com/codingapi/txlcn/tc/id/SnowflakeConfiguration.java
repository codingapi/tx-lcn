package com.codingapi.txlcn.tc.id;

import com.codingapi.txlcn.tc.reporter.TxManagerReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020-8-15 22:53:17
 */
@Configuration
public class SnowflakeConfiguration {

    @Bean
    public SnowflakeStep snowFlakeCreate(TxManagerReporter managerProtocoler){
        return new SnowflakeInfo(managerProtocoler);
    }

}
