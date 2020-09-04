package com.codingapi.txlcn.tm.runner;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.tm.event.TmIdInitEvent;
import com.codingapi.txlcn.tm.util.EventBusUtil;
import com.google.common.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RunnerConfiguration {

    @Autowired
    private ProtocolRunner protocolRunner;

    @Autowired
    private TmNodeServerRunner tmNodeServerRunner;

    @Bean
    public ProtocolRunner protocolRunner(ProtocolServer protocolServer) {
        return new ProtocolRunner(protocolServer);
    }

    @PostConstruct
    public void start() {
        protocolRunner.start();

        // 注册监听器
        Listen tmIdInitEventListen = new Listen();
        EventBusUtil.register(tmIdInitEventListen);
    }

    @Bean
    public TmNodeServerRunner tmNodeServerRunner(ProtocolServer protocolServer) {
        return new TmNodeServerRunner(protocolServer);
    }


    @SuppressWarnings("UnstableApiUsage")
    public class Listen {

        @Subscribe
        public void listenTmIdInitEvent(TmIdInitEvent tmIdInitEvent) {
            System.out.println("listenTmIdInitEvent" + tmIdInitEvent);
            tmNodeServerRunner.init();
        }
    }


}
