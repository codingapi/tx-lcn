package com.codingapi.txlcn.tc.runner;

import com.codingapi.maven.uml.annotation.Model;
import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.tc.config.TxConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Model(flag = "C",value = "TM服务初始化",color = "#FF88EE")
public class TMServerRunner {

  private TxConfig txConfig;

  private ProtocolServer protocolServer;

  /**
   * 初始化连接
   */
  public void init() {
    txConfig.txManagerAddresses().forEach(address->{
      protocolServer.connectTo(address.getHostString(),address.getPort());
    });
  }

}
