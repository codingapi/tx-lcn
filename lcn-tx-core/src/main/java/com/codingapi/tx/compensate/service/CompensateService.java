package com.codingapi.tx.compensate.service;


/**
 * create by lorne on 2017/11/11
 */
public interface CompensateService {

    void saveLocal(String modelName, String uniqueKey, String data, String method, String className, String json);
}
