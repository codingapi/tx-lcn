package com.codingapi.tx.springcloud.listener;

import com.codingapi.tx.listener.service.InitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class ServerListener implements ApplicationListener<WebServerInitializedEvent> {

    private Logger logger = LoggerFactory.getLogger(ServerListener.class);

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private InitService initService;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        logger.info("onApplicationEvent -> onApplicationEvent. "+event.getWebServer());
        this.serverPort = event.getWebServer().getPort();

        Thread thread = new Thread(() -> {
            // 若连接不上txmanager start()方法将阻塞
            initService.start();
        });
        thread.setName("TxInit-thread");
        thread.start();
    }

    public int getServerPort() {
        return serverPort;
    }
}
