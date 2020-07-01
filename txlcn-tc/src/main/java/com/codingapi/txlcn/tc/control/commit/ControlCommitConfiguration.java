package com.codingapi.txlcn.tc.control.commit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Configuration
public class ControlCommitConfiguration {

    @Bean
    public LcnCommitor lcnCommitor(){
        return new LcnCommitor();
    }


}
