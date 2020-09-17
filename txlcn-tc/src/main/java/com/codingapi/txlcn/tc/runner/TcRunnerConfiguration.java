package com.codingapi.txlcn.tc.runner;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.tc.config.TxConfig;
import com.codingapi.txlcn.tc.id.SnowflakeStep;
import com.codingapi.txlcn.tc.jdbc.JdbcTransactionInitializer;
import com.codingapi.txlcn.tc.reporter.TxManagerReporter;
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
  private TmServerRunner tmServerRunner;

  @Autowired
  private JdbcTransactionInitializer jdbcTransactionInitializer;

  @Bean
  public TmServerRunner tmServerRunner(TxConfig txConfig, ProtocolServer protocolServer,
                                       SnowflakeStep snowFlakeStep, TxManagerReporter txManagerReporter) {
    return new TmServerRunner(txConfig, protocolServer, snowFlakeStep, txManagerReporter);
  }

  @SneakyThrows
  @PostConstruct
  public void start() {
    tmServerRunner.init();
    jdbcTransactionInitializer.init(dataSource.getConnection());
  }

}
