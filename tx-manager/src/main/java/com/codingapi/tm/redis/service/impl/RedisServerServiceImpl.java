package com.codingapi.tm.redis.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.tm.config.ConfigReader;
import com.codingapi.tm.netty.model.TxGroup;
import com.codingapi.tm.redis.service.RedisServerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        Set<String> keys =  redisTemplate.keys(configReader.getKeyPrefixCompensate()+"*");
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
    public void saveTransaction(String key, String json) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        value.set(key, json, configReader.getRedisSaveMaxTime(), TimeUnit.SECONDS);
    }


    @Override
    public TxGroup getTxGroupByKey(String key) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        String json = value.get(key);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return  TxGroup.parser(json);
    }


    @Override
    public void saveCompensateMsg(String name, String json) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        value.set(name, json);
    }

    @Override
    public List<String> getKeys(String key) {
        Set<String> keys = redisTemplate.keys(key);
        List<String> list = new ArrayList<String>();
        for (String k : keys) {
            list.add(k);
        }
        return list;
    }

    @Override
    public List<String> getValuesByKeys(List<String> keys) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        List<String> list = new ArrayList<>();
        for (String key : keys) {
            String json = value.get(key);
            list.add(json);
        }
        return list;
    }

    @Override
    public String getValueByKey(String key) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        return value.get(key);
    }

    @Override
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void saveLoadBalance(String groupName, String key, String data) {
        HashOperations<String, String, String> value = redisTemplate.opsForHash();
        value.put(groupName,key,data);
    }


    @Override
    public String getLoadBalance(String groupName, String key) {
        HashOperations<String, String, String> value = redisTemplate.opsForHash();
        return value.get(groupName,key);
    }
}
