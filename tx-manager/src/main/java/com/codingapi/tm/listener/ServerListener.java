package com.codingapi.tm.listener;

import com.codingapi.tm.listener.service.InitService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by lorne on 2017/7/1.
 */

@Component
    public class ServerListener implements ServletContextListener {

    private WebApplicationContext springContext;


    private InitService initService;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        springContext = WebApplicationContextUtils
                .getWebApplicationContext(event.getServletContext());
        initService = springContext.getBean(InitService.class);
        //启动netty内嵌服务
        initService.start();
    }


    @Override
    public void contextDestroyed(ServletContextEvent event) {
        initService.close();
    }

}
