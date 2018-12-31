package com.codingapi.tx.client.aspectlog;

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
public class DBConfiguration {


    @Bean
    public AspectLogDbProperties aspectLogDbProperties(){
        return new AspectLogDbProperties();
    }

    @Bean
    public AspectLogDbHelper aspectLogDbHelper(){
        return new AspectLogDbHelper();
    }

}
