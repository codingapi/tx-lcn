package com.codingapi.tm.api.service.impl;


import com.codingapi.tm.api.service.TxManagerService;
import com.codingapi.tm.api.service.TransactionConfirmService;
import com.codingapi.tm.config.ConfigReader;
import com.lorne.core.framework.utils.KidUtils;
import com.codingapi.tm.Constants;
import com.codingapi.tm.listener.model.TxGroup;
import com.codingapi.tm.listener.model.TxInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * Created by lorne on 2017/6/7.
 */
@Service
public class TxManagerServiceImpl implements TxManagerService {



    @Autowired
    private ConfigReader configReader;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Autowired
    private TransactionConfirmService transactionConfirmService;



    private Logger logger = LoggerFactory.getLogger(TxManagerServiceImpl.class);


    @Override
    public TxGroup createTransactionGroup() {
        String groupId = KidUtils.generateShortUuid();
        TxGroup txGroup = new TxGroup();
        txGroup.setStartTime(System.currentTimeMillis());
        txGroup.setGroupId(groupId);
        String key = configReader.getKeyPrefix() + groupId;
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        value.set(key, txGroup.toJsonString(), configReader.getRedisSaveMaxTime(), TimeUnit.SECONDS);
        return txGroup;
    }

    @Override
    public TxGroup addTransactionGroup(String groupId,String uniqueKey, String taskId,int isGroup, String modelName) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        String key = configReader.getKeyPrefix() + groupId;
        String json = value.get(key);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        TxGroup txGroup = TxGroup.parser(json);
        if (txGroup != null) {
            TxInfo txInfo = new TxInfo();
            txInfo.setModelName(modelName);
            txInfo.setKid(taskId);
            txInfo.setAddress(Constants.address);
            txInfo.setIsGroup(isGroup);
            txInfo.setUniqueKey(uniqueKey);
            txGroup.addTransactionInfo(txInfo);
            value.set(key, txGroup.toJsonString(), configReader.getRedisSaveMaxTime(), TimeUnit.SECONDS);
            return txGroup;
        }
        return null;
    }

    @Override
    public  int getTransaction(String groupId, String taskId) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        String key = configReader.getKeyPrefix() + groupId;
        String json = value.get(key);
        if (StringUtils.isEmpty(json)) {
            key = configReader.getKeyPrefixNotify() + groupId;
            json = value.get(key);
            if (StringUtils.isEmpty(json)) {
                return 0;
            }
        }
        TxGroup txGroup = TxGroup.parser(json);

        if(txGroup.getHasOver()==0){
            long nowTime = System.currentTimeMillis();
            long startTime =  txGroup.getStartTime();
            //超时清理数据
            if(nowTime-startTime>(configReader.getRedisSaveMaxTime()*1000)){
                redisTemplate.delete(key);
                return 0;
            }
            return -1;
        }
        boolean res = txGroup.getState() == 1;
        if(!res) {
            return 0;
        }

        for (TxInfo info : txGroup.getList()) {
            if (info.getKid().equals(taskId)) {
                return info.getNotify()==0?1:0;
            }
        }

        return 0;
    }


    @Override
    public boolean clearTransaction(String groupId, String taskId, int isGroup) {
        logger.info("start-clearTransaction->groupId:"+groupId+",taskId:"+taskId);
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        String key = configReader.getKeyPrefix() + groupId;
        String json = value.get(key);
        if (StringUtils.isEmpty(json)) {
            key = configReader.getKeyPrefixNotify() + groupId;
            json = value.get(key);
            if (StringUtils.isEmpty(json)) {
                return false;
            }
        }
        TxGroup txGroup = TxGroup.parser(json);
        boolean res = txGroup.getState() == 1;

        boolean hasSet = false;
        for (TxInfo info : txGroup.getList()) {
            if (info.getKid().equals(taskId)) {
                info.setNotify(1);
                hasSet = true;
            }
        }

        if(hasSet&&res) {
            String pnKey = configReader.getKeyPrefixNotify() + groupId;
            value.set(pnKey, txGroup.toJsonString());
        }

        boolean isOver = true;
        for (TxInfo info : txGroup.getList()) {
            if(isGroup==1) {
                if (info.getIsGroup() == 0 && info.getNotify() == 0) {
                    isOver = false;
                    break;
                }
            }else{
                if (info.getNotify() == 0) {
                    isOver = false;
                    break;
                }
            }
        }

        if (isOver) {
            if(key.startsWith(configReader.getKeyPrefix())) {
                redisTemplate.delete(key);
            }
        }

        logger.info("end-clearTransaction->groupId:"+groupId+",taskId:"+taskId+",res:"+res);
        return res;
    }


    @Override
    public boolean closeTransactionGroup(String groupId,int state) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        String key = configReader.getKeyPrefix() + groupId;
        String json = value.get(key);
        if (StringUtils.isEmpty(json)) {
            return false;
        }
        final TxGroup txGroup = TxGroup.parser(json);
        txGroup.setState(state);
        txGroup.setHasOver(1);
        return transactionConfirmService.confirm(txGroup);
    }


    @Override
    public void dealTxGroup(TxGroup txGroup, boolean hasOk) {
        String key = configReader.getKeyPrefix() + txGroup.getGroupId();
        if (!hasOk) {
            //未通知成功
            if (txGroup.getState() == 1) {
                ValueOperations<String, String> value = redisTemplate.opsForValue();
                String newKey = configReader.getKeyPrefixNotify() + txGroup.getGroupId();
                value.set(newKey, txGroup.toJsonString());
            }

        }
        redisTemplate.delete(key);
    }


    @Override
    public void deleteTxGroup(TxGroup txGroup) {
        String key = configReader.getKeyPrefix() + txGroup.getGroupId();
        redisTemplate.delete(key);
    }


    @Override
    public int getDelayTime() {
        return configReader.getTransactionNettyDelayTime();
    }
}
