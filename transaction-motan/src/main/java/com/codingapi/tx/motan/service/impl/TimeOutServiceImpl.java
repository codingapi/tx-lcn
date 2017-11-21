package com.codingapi.tx.motan.service.impl;

import com.codingapi.tx.Constants;
import com.codingapi.tx.listener.service.TimeOutService;
import com.weibo.api.motan.config.springsupport.BasicServiceConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2017/11/17 13:30
 */
@Service
public class TimeOutServiceImpl implements TimeOutService {


    @Autowired
    private BasicServiceConfigBean basicServiceConfigBean;


    public void loadOutTime() {
        int timeOut = basicServiceConfigBean.getRequestTimeout();
        Constants.maxOutTime = timeOut;
    }
}
