package com.codingapi.tx.dubbo.service.impl;

import com.alibaba.dubbo.config.ProviderConfig;
import com.codingapi.tx.Constants;
import com.codingapi.tx.listener.service.TimeOutService;
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
    public void loadOutTime(int timeOut) {
        try {
            int providerTimeOut = providerConfig.getTimeout();
            Constants.maxOutTime = providerTimeOut;
        }catch (Exception e){
            Constants.maxOutTime = timeOut * 1000;
        }
    }
}
