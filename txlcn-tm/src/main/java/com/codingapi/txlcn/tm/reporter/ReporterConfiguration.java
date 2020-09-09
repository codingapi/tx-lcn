package com.codingapi.txlcn.tm.reporter;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.tm.config.TmConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author whohim
 */
@Configuration
public class ReporterConfiguration {

    @Bean
    public TxManagerReporter txManagerProtocolHandler(ProtocolServer protocolServer, TmConfig tmConfig) {
        return new TxManagerReporter(protocolServer, tmConfig);
    }


}
