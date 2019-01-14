package com.codingapi.txlcn.client.aspectlog;

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
public class AspectDBConfiguration {

    @Bean
    public AspectLogDbHelper aspectLogDbHelper(){
        return new AspectLogDbHelper();
    }

}
