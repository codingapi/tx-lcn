package com.codingapi.tm.redis.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.tm.config.ConfigReader;
import com.codingapi.tm.netty.model.TxGroup;
import com.codingapi.tm.redis.service.RedisServerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class RedisServerServiceImpl implements RedisServerService{


    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Autowired
    private ConfigReader configReader;


    public String loadNotifyJson() {
        Set<String> keys =  redisTemplate.keys(configReader.getKeyPrefixNotify()+"*");
        ValueOperations<String,String> value =  redisTemplate.opsForValue();
        JSONArray jsonArray = new JSONArray();
        for(String key:keys){
            String json = value.get(key);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key",key);
            jsonObject.put("value",JSONObject.parse(json));
            jsonArray.add(jsonObject);
        }
        return jsonArray.toJSONString();
    }

    @Override
    public void createTransactionGroup(String groupId, String json) {
        String key = configReader.getKeyPrefix() + groupId;
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        value.set(key, json, configReader.getRedisSaveMaxTime(), TimeUnit.SECONDS);
    }

    public void updateTransactionGroup(String groupId, String json){
        String key = configReader.getKeyPrefix() + groupId;
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        value.set(key, json, configReader.getRedisSaveMaxTime(), TimeUnit.SECONDS);
    }

    public void updateNotifyTransactionGroup(String groupId, String json){
        String key = configReader.getKeyPrefixNotify() + groupId;
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        value.set(key, json, configReader.getRedisSaveMaxTime(), TimeUnit.SECONDS);
    }

    @Override
    public void deleteTxGroup(String groupId) {
        String key = configReader.getKeyPrefix() + groupId;
        redisTemplate.delete(key);
    }

    @Override
    public void deleteNotifyTxGroup(String groupId) {
        String key = configReader.getKeyPrefixNotify() + groupId;
        redisTemplate.delete(key);
    }

    @Override
    public TxGroup getTxGroupById(String groupId) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        String key = configReader.getKeyPrefix() + groupId;
        String json = value.get(key);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return  TxGroup.parser(json);
    }

    @Override
    public TxGroup getTxGroupOnNotifyById(String groupId) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        String key = configReader.getKeyPrefixNotify() + groupId;
        String json = value.get(key);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return  TxGroup.parser(json);
    }
}
