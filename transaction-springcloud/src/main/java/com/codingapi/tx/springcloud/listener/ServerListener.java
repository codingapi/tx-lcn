package com.codingapi.tx.springcloud.listener;

import com.codingapi.tx.listener.service.InitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class ServerListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    private Logger logger = LoggerFactory.getLogger(ServerListener.class);

    private int serverPort;

    @Autowired
    private InitService initService;

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        logger.info("onApplicationEvent -> onApplicationEvent. "+event.getEmbeddedServletContainer());
        this.serverPort = event.getEmbeddedServletContainer().getPort();

        initService.start();
    }

    public int getPort() {
        return this.serverPort;
    }
}
