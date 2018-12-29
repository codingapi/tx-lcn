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
public class DBConfiguration {


    @Bean
    public DbProperties dbProperties(){
        return new DbProperties();
    }

    @Bean
    public DbHelper dbHelper(){
        return new DbHelper();
    }

}
