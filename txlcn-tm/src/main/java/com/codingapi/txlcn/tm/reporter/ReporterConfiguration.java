package com.codingapi.txlcn.tm.reporter;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.tm.config.TmConfig;
import com.codingapi.txlcn.tm.repository.TmNodeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author whohim
 */
@Configuration
public class ReporterConfiguration {

    @Bean
    public TmManagerReporter txManagerProtocolHandler(ProtocolServer protocolServer, TmConfig tmConfig,
                                                      TmNodeRepository tmNodeRepository) {
        return new TmManagerReporter(protocolServer, tmConfig, tmNodeRepository);
    }


}
