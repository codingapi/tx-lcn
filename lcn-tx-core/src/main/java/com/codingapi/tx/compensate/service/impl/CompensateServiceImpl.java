package com.codingapi.tx.compensate.service.impl;

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
    public void saveLocal(String modelName, String uniqueKey, String data, String method, String className, String json) {
        logger.warn("state:"+json+",modelName:"+modelName+"," +
            "uniqueKey:"+uniqueKey+",method:"+method+",className:"+className+",data:"+data);
    }
}
