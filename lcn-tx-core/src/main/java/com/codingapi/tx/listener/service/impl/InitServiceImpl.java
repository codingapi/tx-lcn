package com.codingapi.tx.listener.service.impl;

import com.codingapi.tx.Constants;
import com.codingapi.tx.datasource.service.DataSourceService;
import com.codingapi.tx.listener.service.NettyService;
import com.codingapi.tx.listener.service.InitService;
import com.codingapi.tx.listener.service.ModelNameService;
import com.codingapi.tx.listener.service.TimeOutService;
import com.codingapi.tx.datasource.ILCNDataSourceProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuliang on 2017/7/11.
 */
@Service
public class InitServiceImpl implements InitService {

    private final static Logger logger = LoggerFactory.getLogger(InitServiceImpl.class);

    @Autowired
    private NettyService nettyService;

    @Autowired
    private TimeOutService timeOutService;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private ILCNDataSourceProxy baseProxy;

    @Autowired
    private ModelNameService modelNameService;

    @Override
    public void start() {

        /**
         * 由于SQLSessionFactory等类的加载方式导致，无法通过
         * bean自动注入的方式注入给代理对象，因此通过初始化的时候再为其赋值。
         */
        baseProxy.setDataSourceService(dataSourceService);

        /**
         * 设置模块唯一标示
         */

        Constants.uniqueKey = modelNameService.getUniqueKey();

        nettyService.start();
        logger.info("socket-start..");

        timeOutService.loadOutTime();

        logger.info("check-compensate-running..");

    }
}
