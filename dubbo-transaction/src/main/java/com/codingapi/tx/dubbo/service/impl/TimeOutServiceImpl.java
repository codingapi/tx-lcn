package com.codingapi.tx.dubbo.service.impl;

import com.alibaba.dubbo.config.ProviderConfig;
import com.codingapi.tx.Constants;
import com.codingapi.tx.service.TimeOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/8/7
 */
@Service
public class TimeOutServiceImpl implements TimeOutService {


    @Autowired
    private ProviderConfig providerConfig;


    @Override
    public void loadOutTime() {
        int timeOut = providerConfig.getTimeout();
        Constants.maxOutTime = timeOut;
    }
}
