package com.codingapi.tx.logger.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/20
 *
 * @author codingapi
 */
@Configuration
public class LogDBConfiguration {


    @Bean
    public LogDbProperties logDbProperties(){
        return new LogDbProperties();
    }

    @Bean
    public LogDbHelper logDbHelper(){
        return new LogDbHelper();
    }

}
