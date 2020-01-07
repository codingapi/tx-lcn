package com.codingapi.txlcn.tc.runner;

import com.codingapi.txlcn.protocol.Config;
import com.codingapi.txlcn.protocol.client.PeerClientHandle;
import com.codingapi.txlcn.tc.config.TxConfig;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class TMServerRunnerTest {

  private TxConfig txConfig;

  private PeerClientHandle peerClientHandle;

  private TMServerRunner serverRunner;

  @Before
  public void before() {
    txConfig = new TxConfig(new Config());
    txConfig.setTms(Lists.newArrayList("127.0.0.1:8070,127.0.0.1:8072"));
    peerClientHandle = Mockito.mock(PeerClientHandle.class);
    serverRunner = new TMServerRunner(txConfig, peerClientHandle);
  }

  @Test
  public void init() throws Exception {
    serverRunner.init();
  }

}