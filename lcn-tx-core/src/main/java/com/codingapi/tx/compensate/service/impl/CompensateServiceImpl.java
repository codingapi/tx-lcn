package com.codingapi.tx.compensate.service.impl;

import com.alibaba.fastjson.JSON;
import com.codingapi.tx.compensate.model.CompensateInfo;
import com.codingapi.tx.compensate.service.CompensateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class CompensateServiceImpl implements CompensateService {


    private Logger logger = LoggerFactory.getLogger(CompensateServiceImpl.class);

    @Override
    public void saveLocal(CompensateInfo compensateInfo) {
        String json = JSON.toJSONString(compensateInfo);
        logger.info("补偿本地记录->" + json);

    }
}
