package com.codingapi.txlcn.tc.runner;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.tc.config.TxConfig;
import com.codingapi.txlcn.tc.jdbc.JdbcTransactionInitializer;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class TcRunnerConfiguration {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private TMServerRunner tmServerRunner;

  @Autowired
  private JdbcTransactionInitializer jdbcTransactionInitializer;

  @Bean
  public TMServerRunner tmServerRunner(TxConfig txConfig, ProtocolServer protocolServer) {
    return new TMServerRunner(txConfig, protocolServer);
  }

  @SneakyThrows
  @PostConstruct
  public void start() {
    tmServerRunner.init();
    jdbcTransactionInitializer.init(dataSource.getConnection());
  }

}
