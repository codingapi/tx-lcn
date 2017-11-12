package com.codingapi.tm.api.service.impl;

import com.codingapi.tm.api.service.ApiAdminService;
import com.codingapi.tm.manager.service.EurekaService;
import com.codingapi.tm.model.TxState;
import com.codingapi.tm.redis.service.RedisServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/11/12
 */
@Service
public class ApiAdminServiceImpl implements ApiAdminService {


    @Autowired
    private EurekaService eurekaService;

    @Autowired
    private RedisServerService redisServerService;

    @Override
    public TxState getState() {
        return eurekaService.getState();
    }

    @Override
    public String loadNotifyJson() {
        return redisServerService.loadNotifyJson();
    }
}
