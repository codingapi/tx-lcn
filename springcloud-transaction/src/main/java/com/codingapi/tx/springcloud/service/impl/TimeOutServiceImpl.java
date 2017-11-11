package com.codingapi.tx.springcloud.service.impl;

import com.codingapi.tx.Constants;
import com.codingapi.tx.service.TimeOutService;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/8/7
 */
@Service
public class TimeOutServiceImpl implements TimeOutService {


    @Override
    public void loadOutTime() {
        //todo 暂时写死
        int timeOut = 20*1000;
        Constants.maxOutTime = timeOut;
    }
}
