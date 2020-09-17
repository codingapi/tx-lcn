package com.codingapi.txlcn.tc.reporter;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.tc.config.TxConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lorne
 * @date 2020/4/3
 * @description
 */
@Configuration
public class ReporterConfiguration {

    @Bean
    public TxManagerReporter txManagerProtocoler(ProtocolServer protocolServer, TxConfig txConfig){
        return new TxManagerReporter(protocolServer,txConfig);
    }


}
