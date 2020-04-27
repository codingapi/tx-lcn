package com.codingapi.txlcn.tc.protocol;

import com.codingapi.txlcn.protocol.ProtocolServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lorne
 * @date 2020/4/3
 * @description
 */
@Configuration
public class ProtocolerConfiguration {

    @Bean
    public TxManagerProtocoler txManagerProtocoler(ProtocolServer protocolServer){
        return new TxManagerProtocoler(protocolServer);
    }


}
