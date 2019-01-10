package com.codingapi.tx.spi.sleuth.dubbo;

import com.codingapi.tx.spi.sleuth.listener.SleuthParamListener;
import com.codingapi.tx.spi.sleuth.dubbo.loadbalance.TXLCNLoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/14
 *
 * @author ujued
 */
@Configuration
@ComponentScan
@Slf4j
public class TxDubboConfiguration {

    static {
        System.setProperty("dubbo.provider.filter","tracing");
        System.setProperty("dubbo.consumer.filter","tracing");
    }

    @Autowired
    private SleuthParamListener sleuthParamListener;

    @PostConstruct
    public void init(){
        TXLCNLoadBalance.sleuthParamListener = sleuthParamListener;
        log.info("init sleuthParamListener->{}",sleuthParamListener);
    }
}
