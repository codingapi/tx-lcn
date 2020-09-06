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

    /**
     * <p>为了获得该节点的全局唯一 ID ，需等待雪花算法初始化结束</p>
     * <p>连接其他 TM 节点</p>
     * <p>观察者模式解耦</p>
     */
    @SuppressWarnings({"UnstableApiUsage", "unused"})
    public class Listen {

        @Subscribe
        public void listenTmIdInitEvent(TmIdInitEvent tmIdInitEvent) {
            tmNodeServerRunner.init(tmIdInitEvent.getTmId());
        }
    }


}
