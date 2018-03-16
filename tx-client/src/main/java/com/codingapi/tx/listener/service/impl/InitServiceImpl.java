package com.codingapi.tx.listener.service.impl;

import com.codingapi.tx.listener.service.InitService;
import com.codingapi.tx.netty.service.NettyService;
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

    @Override
    public void start() {
        nettyService.start();
        logger.info("socket-start..");
    }

}
