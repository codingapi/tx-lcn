package com.codingapi.txlcn.tc.runner;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.protocol.config.Config;
import com.codingapi.txlcn.tc.config.TxConfig;
import com.codingapi.txlcn.tc.id.SnowflakeStep;
import com.codingapi.txlcn.tc.reporter.TxManagerReporter;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;


 class TmServerRunnerTest {

  private TxConfig txConfig;

  private ProtocolServer protocolServer;

  @MockBean
  private TmServerRunner serverRunner;

  @Autowired
  private ApplicationContext springContext;

  @Autowired
  private SnowflakeStep snowFlakeStep;

  @Autowired
  private TxManagerReporter txManagerReporter;

     @BeforeEach
  void before() {
    Config protocolConfig = new Config();
    txConfig = new TxConfig(protocolConfig);
    txConfig.setTms(Lists.newArrayList("127.0.0.1:8070,127.0.0.1:8072"));
    protocolServer = new ProtocolServer(protocolConfig,springContext);
    serverRunner = new TmServerRunner(txConfig, protocolServer,snowFlakeStep, txManagerReporter);
  }

  @Test
  public void init() throws Exception {
    serverRunner.init();
  }

}
