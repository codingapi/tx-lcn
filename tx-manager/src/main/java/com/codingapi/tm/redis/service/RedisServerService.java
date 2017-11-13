package com.codingapi.tm.redis.service;

import com.codingapi.tm.netty.model.TxGroup;

import java.util.List;

/**
 * create by lorne on 2017/11/11
 */
public interface RedisServerService {

    String loadNotifyJson();

    void createTransactionGroup(String groupId, String json);

    TxGroup getTxGroupById(String groupId);

    TxGroup getTxGroupOnNotifyById(String groupId);

    void updateTransactionGroup(String groupId, String json);

    void updateNotifyTransactionGroup(String groupId, String json);

    void deleteTxGroup(String groupId);

    void deleteNotifyTxGroup(String groupId);

    void saveCompensateMsg(String name, String json);

    List<String> getKeys(String key);

    List<String> getValuesByKeys(List<String> keys);

    String getValueByKey(String key);

    void deleteKey(String key);
}
