package com.lorne.tx.listener;

import com.lorne.tx.service.InitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by lorne on 2017/7/1.
 */
@Component
public class TransactionSocketListener implements ApplicationContextAware {


    @Autowired
    private InitService initService;


    @Override
    public void setApplicationContext(ApplicationContext event) throws BeansException {
        initService.start();
    }

}
