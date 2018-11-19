package com.codingapi.tx.springcloud.listener;

import com.codingapi.tx.listener.service.InitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class ServerListener implements  ApplicationListener<ApplicationStartedEvent> {

    private Logger logger = LoggerFactory.getLogger(ServerListener.class);

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private InitService initService;


    public int getPort() {
        return this.serverPort;
    }
    
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        logger.info("onApplicationEvent -> onApplicationEvent. "+event.getClass());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 若连接不上txmanager start()方法将阻塞
                initService.start();
            }
        });
        thread.setName("TxInit-thread");
        thread.start();
    }
}
