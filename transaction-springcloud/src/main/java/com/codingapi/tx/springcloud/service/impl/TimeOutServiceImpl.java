package com.codingapi.tx.springcloud.service.impl;

import com.codingapi.tx.Constants;
import com.codingapi.tx.listener.service.TimeOutService;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/8/7
 */
@Service
public class TimeOutServiceImpl implements TimeOutService {


    @Override
    public void loadOutTime(int timeOut) {
    	//从txManager取
    	if(timeOut <= 0){
    		Constants.maxOutTime = 20*1000;
    	} else {
    		Constants.maxOutTime = timeOut*1000;
    	}
    }
}
