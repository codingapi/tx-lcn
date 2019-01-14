/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.manager.db.redis;

import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.commons.exception.JoinGroupException;
import com.codingapi.txlcn.manager.config.TxManagerConfig;
import com.codingapi.txlcn.manager.core.group.GroupRelationship;
import com.codingapi.txlcn.manager.core.group.TransUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * <p>基于Redis实现的事务组关系</p>
 * Date: 2018/12/4
 *
 * @author ujued
 */
@Service
@Slf4j
public class RedisGroupRelationship implements GroupRelationship {

    private static final String REDIS_PREFIX = "tx.manager:group:";

    private static final String REDIS_GROUP_STATE = REDIS_PREFIX + ":state";

    private final RedisTemplate<String, String> redisTemplate;

    private final TxManagerConfig managerConfig;

    @Autowired
    public RedisGroupRelationship(RedisTemplate<String, String> redisTemplate, TxManagerConfig managerConfig) {
        this.redisTemplate = redisTemplate;
        this.managerConfig = managerConfig;
    }

    @Override
    public void createGroup(String groupId) {
        redisTemplate.opsForList().leftPush(REDIS_PREFIX + groupId, "tx.starter");
        redisTemplate.expire(REDIS_PREFIX + groupId, managerConfig.getDtxTime() + 10000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void joinGroup(String groupId, TransUnit transUnit) throws JoinGroupException {
        if (Optional.ofNullable(redisTemplate.hasKey(REDIS_PREFIX + groupId)).orElse(false)) {
            redisTemplate.opsForList().rightPush(REDIS_PREFIX + groupId, JSON.toJSONString(transUnit));
            return;
        }
        log.warn("attempts to join non-existent transaction group:{} !", groupId);
        throw new JoinGroupException("attempts to join non-existent transaction group " + groupId);
    }

    @Override
    public List<TransUnit> unitsOfGroup(String groupId) {
        Long size = redisTemplate.opsForList().size(REDIS_PREFIX + groupId);
        if (Objects.isNull(size)) {
            throw new IllegalStateException("non exists this group.");
        }
        List<String> units = redisTemplate.opsForList().range(REDIS_PREFIX + groupId, 1, size);
        if (Objects.isNull(units)) {
            throw new IllegalStateException("non exists this group.");
        }
        log.debug("transaction units: {}", units);
        List<TransUnit> transUnits = new ArrayList<>(units.size());
        units.forEach(unit -> transUnits.add(JSON.parseObject(unit, TransUnit.class)));
        return transUnits;
    }

    @Override
    public void removeGroup(String groupId) {
        log.debug("remove group:{} from redis.", groupId);
        redisTemplate.delete(REDIS_PREFIX + groupId);
    }

    @Override
    public void setTransactionState(String groupId, int state) {
        redisTemplate.opsForValue().set(REDIS_GROUP_STATE + groupId, String.valueOf(state));
        redisTemplate.expire(REDIS_GROUP_STATE + groupId, managerConfig.getDtxTime() + 10000, TimeUnit.MILLISECONDS);
    }

    @Override
    public Short transactionState(String groupId) {
        String state = redisTemplate.opsForValue().get(REDIS_GROUP_STATE + groupId);
        if (Objects.isNull(state)) {
            return 0;
        }
        try {
            return Short.valueOf(state);
        } catch (Exception e) {
            return 0;
        }
    }
}
