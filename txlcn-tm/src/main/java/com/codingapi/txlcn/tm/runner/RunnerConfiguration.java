package com.codingapi.txlcn.tm.runner;

import com.codingapi.txlcn.protocol.manager.PeerHandle;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RunnerConfiguration {

  @Autowired
  private ProtocolRunner protocolRunner;

  @Bean
  public ProtocolRunner protocolRunner(PeerHandle peerHandle) {
    return new ProtocolRunner(peerHandle);
  }

  @PostConstruct
  public void start() {
    protocolRunner.start();
  }

}
