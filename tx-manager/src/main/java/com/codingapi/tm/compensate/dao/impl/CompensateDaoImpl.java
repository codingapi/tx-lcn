package com.codingapi.tm.compensate.dao.impl;

import com.alibaba.fastjson.JSON;
import com.codingapi.tm.compensate.dao.CompensateDao;
import com.codingapi.tm.compensate.model.TransactionCompensateMsg;
import com.codingapi.tm.netty.model.TxGroup;
import com.codingapi.tm.redis.service.RedisServerService;
import com.lorne.core.framework.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class CompensateDaoImpl implements CompensateDao {


    @Autowired
    private RedisServerService redisServerService;

    private final static String prefix = "compensate_";

    @Override
    public boolean saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg) {

        String name = String.format("%s%s_%s_%s.json", prefix, transactionCompensateMsg.getModel(), DateUtil.getCurrentDateFormat(), transactionCompensateMsg.getGroupId());

        String json = JSON.toJSONString(transactionCompensateMsg);

        redisServerService.saveCompensateMsg(name, json);

        return true;
    }


    @Override
    public List<String> loadModelList() {
        String key = prefix + "*";
        List<String> keys = redisServerService.getKeys(key);
        List<String> models = new ArrayList<>();
        for (String k : keys) {
            String name = k.split("_")[1];
            if (!models.contains(name)) {
                models.add(name);
            }
        }
        return models;
    }


    @Override
    public List<String> loadCompensateTimes(String model) {
        String key = prefix + model + "_*";
        List<String> keys = redisServerService.getKeys(key);
        List<String> times = new ArrayList<>();
        for (String k : keys) {
            String time = k.split("_")[2];
            if (!times.contains(time)) {
                times.add(time);
            }
        }
        return times;
    }


    @Override
    public List<String> loadCompensateByModelAndTime(String path) {
        String key = String.format("%s%s*", prefix, path);
        List<String> keys = redisServerService.getKeys(key);
        List<String> values = redisServerService.getValuesByKeys(keys);
        return values;
    }

    @Override
    public String getCompensate(String path) {
        String key = String.format("%s%s.json", prefix, path);
        return redisServerService.getValueByKey(key);
    }


    @Override
    public String getCompensateByGroupId(String groupId) {
        String key = String.format("%s*%s.json", prefix, groupId);
        List<String> keys = redisServerService.getKeys(key);
        if (keys != null && keys.size() == 1) {
            String k = keys.get(0);
            return redisServerService.getValueByKey(k);
        }
        return null;
    }
}
