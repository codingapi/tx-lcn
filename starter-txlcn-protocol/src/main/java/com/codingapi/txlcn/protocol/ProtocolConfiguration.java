package com.codingapi.txlcn.protocol;

import com.codingapi.txlcn.protocol.client.PeerClientHandle;
import com.codingapi.txlcn.protocol.config.PeerConfig;
import com.codingapi.txlcn.protocol.manager.PeerHandle;
import com.codingapi.txlcn.protocol.manager.service.ConnectionService;
import com.codingapi.txlcn.protocol.manager.service.IPingService;
import com.codingapi.txlcn.protocol.manager.service.LeadershipService;
import com.codingapi.txlcn.protocol.manager.service.NoForwardPingService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
public class ProtocolConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "txlcn.protocol")
    @ConditionalOnMissingBean
    public PeerConfig peerConfig(){
        return new PeerConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public PeerEventLoopGroup peerEventLoopGroup(PeerConfig peerConfig){
        return new PeerEventLoopGroup(peerConfig.getName(),peerConfig.getPort());
    }

    @Bean
    @ConditionalOnMissingBean
    public ConnectionService connectionService(PeerEventLoopGroup peerEventLoopGroup){
        return new ConnectionService(peerEventLoopGroup);
    }

    @Bean
    @ConditionalOnMissingBean
    public LeadershipService leadershipService(ConnectionService connectionService, PeerEventLoopGroup peerEventLoopGroup){
        return new LeadershipService(connectionService,peerEventLoopGroup);
    }

    @Bean
    @ConditionalOnMissingBean
    public IPingService pingService(ConnectionService connectionService, LeadershipService leadershipService, PeerEventLoopGroup peerEventLoopGroup){
        return new NoForwardPingService(connectionService,leadershipService,peerEventLoopGroup);
    }

    @Bean
    @ConditionalOnMissingBean
    public PeerHandle peerHandle(PeerConfig peerConfig, PeerEventLoopGroup peerEventLoopGroupBean, ConnectionService connectionService, LeadershipService leadershipService, IPingService pingService){
        return new PeerHandle(new Config(peerConfig.getName()),peerConfig.getPort(),peerEventLoopGroupBean,connectionService,leadershipService,pingService);
    }

    @Bean
    @ConditionalOnMissingBean
    public PeerClientHandle peerClientHandle(PeerEventLoopGroup peerEventLoopGroup){
        return new PeerClientHandle(peerEventLoopGroup);
    }
}
