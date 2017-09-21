package com.lorne.tx.springcloud.service.impl;

import com.lorne.tx.Constants;
import com.lorne.tx.service.TimeOutService;
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
