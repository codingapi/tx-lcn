package com.codingapi.tx.compensate.service;


/**
 * create by lorne on 2017/11/11
 */
public interface CompensateService {

    void saveLocal(long currentTime, String modelName, String uniqueKey, String data, String methodStr, String className, String json);
}
