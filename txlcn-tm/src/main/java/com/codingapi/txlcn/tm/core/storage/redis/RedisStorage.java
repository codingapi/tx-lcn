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
package com.codingapi.txlcn.tm.core.storage.redis;

import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.common.exception.FastStorageException;
import com.codingapi.txlcn.common.lock.DTXLocks;
import com.codingapi.txlcn.common.util.ApplicationInformation;
import com.codingapi.txlcn.tm.cluster.TMProperties;
import com.codingapi.txlcn.tm.config.TxManagerConfig;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import com.codingapi.txlcn.tm.core.storage.GroupProps;
import com.codingapi.txlcn.tm.core.storage.LockValue;
import com.codingapi.txlcn.tm.core.storage.TransactionUnit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description:
 * Date: 19-1-21 下午3:22
 *
 * @author ujued
 */
@Slf4j
@Data
public class RedisStorage implements FastStorage {

    private static final String REDIS_GROUP_PREFIX = "tm:group:";

    private static final String REDIS_GROUP_STATE = REDIS_GROUP_PREFIX + "transactionState:";

    private static final String REDIS_TOKEN_PREFIX = "tm.token";

    private static final String REDIS_TM_LIST = "tm.instances";

    private static final String REDIS_MACHINE_ID_MAP = "tm.machine.id.gen";

    private RedisTemplate<String, Object> redisTemplate;

    private TxManagerConfig managerConfig;

    public RedisStorage() {
    }

    public RedisStorage(RedisTemplate<String, Object> redisTemplate, TxManagerConfig managerConfig) {
        this.redisTemplate = redisTemplate;
        this.managerConfig = managerConfig;
    }

    @Override
    public void initGroup(GroupProps groupProps) {
        redisTemplate.opsForHash().put(REDIS_GROUP_PREFIX + groupProps.getGroupId(), "root", groupProps);
        redisTemplate.expire(REDIS_GROUP_PREFIX + groupProps.getGroupId(),
                managerConfig.getDtxTime() + 10000, TimeUnit.MILLISECONDS);
    }

    @Override
    public GroupProps getGroupProps(String groupId) throws FastStorageException {
        return (GroupProps) redisTemplate.opsForHash().get(REDIS_GROUP_PREFIX + groupId, "root");
    }

    @Override
    public boolean containsTransactionUnit(String groupId, TransactionUnit transactionUnit) {
        Object unit = redisTemplate.opsForHash().get(REDIS_GROUP_PREFIX + groupId, transactionUnit.getUnitId());
        return Objects.nonNull(unit);
    }

    @Override
    public boolean containsGroup(String groupId) {
        return Optional.ofNullable(redisTemplate.hasKey(REDIS_GROUP_PREFIX + groupId)).orElse(false);
    }

    @Override
    public List<TransactionUnit> findTransactionUnitsFromGroup(String groupId) throws FastStorageException {
        Map<Object, Object> units = redisTemplate.opsForHash().entries(REDIS_GROUP_PREFIX + groupId);
        return units.entrySet().stream()
                .filter(objectObjectEntry -> !objectObjectEntry.getKey().equals("root"))
                .map(objectObjectEntry -> (TransactionUnit) objectObjectEntry.getValue()).collect(Collectors.toList());
    }

    @Override
    public void saveTransactionUnitToGroup(String groupId, TransactionUnit transactionUnit) throws FastStorageException {
        if (Optional.ofNullable(redisTemplate.hasKey(REDIS_GROUP_PREFIX + groupId)).orElse(false)) {
            redisTemplate.opsForHash().put(REDIS_GROUP_PREFIX + groupId, transactionUnit.getUnitId(), transactionUnit);
            return;
        }
        throw new FastStorageException("attempts to the non-existent transaction group " + groupId,
                FastStorageException.EX_CODE_NON_GROUP);
    }

    @Override
    public void clearGroup(String groupId) {
        log.debug("remove group:{} from redis.", groupId);
        redisTemplate.delete(REDIS_GROUP_PREFIX + groupId);
    }

    @Override
    public void saveTransactionState(String groupId, int state) throws FastStorageException {
        redisTemplate.opsForValue().set(REDIS_GROUP_STATE + groupId, String.valueOf(state));
        redisTemplate.expire(REDIS_GROUP_STATE + groupId, managerConfig.getDtxTime() + 10000, TimeUnit.MILLISECONDS);
    }

    @Override
    public int getTransactionState(String groupId) throws FastStorageException {
        Object state = redisTemplate.opsForValue().get(REDIS_GROUP_STATE + groupId);
        if (Objects.isNull(state)) {
            return -1;
        }

        try {
            return Integer.valueOf(state.toString());
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void acquireLocks(String contextId, Set<String> locks, LockValue lockValue) throws FastStorageException {

        // 未申请锁则为申请正常
        if (Objects.isNull(locks)) {
            return;
        }

        Set<String> successLocks = new HashSet<>();
        try {
            for (String lockId : locks) {
                String globalLockId = contextId + lockId;
                LockValue lock = (LockValue) redisTemplate.opsForValue().get(globalLockId);
                if (Objects.nonNull(lock)) {
                    // 不在同一个DTX下，已存在的锁是排它锁 或者 新请求的不是共享锁时， 获取锁失败
                    if (!lockValue.getGroupId().equals(lock.getGroupId()) &&
                            (lock.getLockType() == DTXLocks.X_LOCK || lockValue.getLockType() != DTXLocks.S_LOCK)) {
                        throw new FastStorageException("repeat x_lock", FastStorageException.EX_CODE_REPEAT_LOCK);
                    }
                    // 直接成功
                    return;
                }
                redisTemplate.opsForValue().set(globalLockId, lockValue, managerConfig.getDtxLockTime(), TimeUnit.MILLISECONDS);
                successLocks.add(globalLockId);
            }
        } catch (FastStorageException e) {
            redisTemplate.delete(successLocks);
            throw e;
        }
    }

    @Override
    public void releaseLocks(String cate, Set<String> locks) {
        redisTemplate.delete(locks.stream().map(lock -> (cate + lock)).collect(Collectors.toSet()));
    }

    @Override
    public void saveToken(String token) {
        Objects.requireNonNull(token);
        redisTemplate.opsForList().leftPush(REDIS_TOKEN_PREFIX, token);
        redisTemplate.expire(REDIS_TOKEN_PREFIX, 20, TimeUnit.MINUTES);

        Long size = redisTemplate.opsForList().size(REDIS_TOKEN_PREFIX);
        if (Objects.nonNull(size) && size > 3) {
            redisTemplate.opsForList().rightPop(REDIS_TOKEN_PREFIX);
        }
    }

    @Override
    public List<String> findTokens() {
        Long size = redisTemplate.opsForList().size(REDIS_TOKEN_PREFIX);
        if (Objects.isNull(size)) {
            return Collections.emptyList();
        }
        return Objects.requireNonNull(redisTemplate.opsForList().range(REDIS_TOKEN_PREFIX, 0, size))
                .stream()
                .map(Object::toString).collect(Collectors.toList());
    }

    @Override
    public void removeToken(String token) {
        redisTemplate.delete(REDIS_TOKEN_PREFIX);
    }

    @Override
    public void saveTMProperties(TMProperties tmProperties) {
        Objects.requireNonNull(tmProperties);
        redisTemplate.opsForHash().put(REDIS_TM_LIST,
                tmProperties.getHost() + ":" + tmProperties.getTransactionPort(), tmProperties.getHttpPort());
    }

    @Override
    public List<TMProperties> findTMProperties() {
        return redisTemplate.opsForHash().entries(REDIS_TM_LIST).entrySet().stream()
                .map(entry -> {
                    String[] args = ApplicationInformation.splitAddress(entry.getKey().toString());
                    TMProperties tmProperties = new TMProperties();
                    tmProperties.setHost(args[0]);
                    tmProperties.setTransactionPort(Integer.valueOf(args[1]));
                    tmProperties.setHttpPort(Integer.parseInt(entry.getValue().toString()));
                    return tmProperties;
                }).collect(Collectors.toList());
    }

    @Override
    public void removeTMProperties(String host, int transactionPort) {
        Objects.requireNonNull(host);
        redisTemplate.opsForHash().delete(REDIS_TM_LIST, host + ":" + transactionPort);
        log.debug("removed TM {}:{}", host, transactionPort);
    }


    @Override
    public int acquireMachineId(String key, int size) throws FastStorageException {
        if (redisTemplate.opsForHash().hasKey(REDIS_MACHINE_ID_MAP, key)) {
            return (int) redisTemplate.opsForHash().get(REDIS_MACHINE_ID_MAP, key);
        }
        redisTemplate.opsForHash().putIfAbsent(REDIS_MACHINE_ID_MAP, "cur_id", -1);
        int curId = (int) redisTemplate.opsForHash().get(REDIS_MACHINE_ID_MAP, "cur_id");
        for (int i = 0; i < size; i++) {
            ++curId;
            if (redisTemplate.opsForHash().values(REDIS_MACHINE_ID_MAP).contains((curId) &= Integer.MAX_VALUE)) {
                continue;
            }
            redisTemplate.opsForHash().put(REDIS_MACHINE_ID_MAP, "cur_id", curId);
            redisTemplate.opsForHash().put(REDIS_MACHINE_ID_MAP, key, curId);
            return curId;
        }
        throw new FastStorageException("non can used", FastStorageException.EX_CODE_NON_MACHINE_ID);
    }

    @Override
    public void releaseMachineId(String key) {
        redisTemplate.opsForHash().delete(REDIS_MACHINE_ID_MAP, key);
    }
}
