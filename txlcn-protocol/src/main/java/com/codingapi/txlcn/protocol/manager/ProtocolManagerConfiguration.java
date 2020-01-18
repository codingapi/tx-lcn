package com.codingapi.txlcn.protocol.manager;

import com.codingapi.txlcn.protocol.Config;
import com.codingapi.txlcn.protocol.PeerEventLoopGroup;
import com.codingapi.txlcn.protocol.config.PeerConfig;
import com.codingapi.txlcn.protocol.manager.service.ConnectionService;
import com.codingapi.txlcn.protocol.manager.service.IPingService;
import com.codingapi.txlcn.protocol.manager.service.LeadershipService;
import com.codingapi.txlcn.protocol.manager.service.NoForwardPingService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProtocolManagerConfiguration {


  @Bean
  @ConditionalOnMissingBean
  public ConnectionService connectionService(PeerEventLoopGroup peerEventLoopGroup) {
    return new ConnectionService(peerEventLoopGroup);
  }

  @Bean
  @ConditionalOnMissingBean
  public LeadershipService leadershipService(ConnectionService connectionService,
      PeerEventLoopGroup peerEventLoopGroup) {
    return new LeadershipService(connectionService, peerEventLoopGroup);
  }

  @Bean
  @ConditionalOnMissingBean
  public IPingService pingService(ConnectionService connectionService,
      LeadershipService leadershipService, PeerEventLoopGroup peerEventLoopGroup) {
    return new NoForwardPingService(connectionService, leadershipService, peerEventLoopGroup);
  }

  @Bean
  @ConditionalOnMissingBean
  public TMHandle peerHandle(PeerConfig peerConfig, PeerEventLoopGroup peerEventLoopGroupBean,
      ConnectionService connectionService, LeadershipService leadershipService,
      IPingService pingService) {
    return new TMHandle(new Config(peerConfig.getName()), peerConfig.getPort(),
        peerEventLoopGroupBean, connectionService, leadershipService, pingService);
  }

}
