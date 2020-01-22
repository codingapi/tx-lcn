package com.codingapi.txlcn.tc.runner;

import com.codingapi.txlcn.protocol.Config;
import com.codingapi.txlcn.protocol.PeerEventLoopGroup;
import com.codingapi.txlcn.protocol.client.TCHandle;
import com.codingapi.txlcn.tc.config.TxConfig;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;


public class TMServerRunnerTest {

  private TxConfig txConfig;

  private TCHandle tcHandle;

  @MockBean
  private TMServerRunner serverRunner;

  @Before
  public void before() {
    txConfig = new TxConfig(new Config());
    txConfig.setTms(Lists.newArrayList("127.0.0.1:8070,127.0.0.1:8072"));
    tcHandle = new TCHandle(new PeerEventLoopGroup("127.0.0.1",8265));
    serverRunner = new TMServerRunner(txConfig, tcHandle);
  }

  @Test
  public void init() throws Exception {
    serverRunner.init();
  }

}