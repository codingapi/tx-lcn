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

import com.codingapi.txlcn.common.exception.FastStorageException;
import com.codingapi.txlcn.common.lock.DTXLocks;
import com.codingapi.txlcn.common.util.ApplicationInformation;
import com.codingapi.txlcn.tm.cluster.TMProperties;
import com.codingapi.txlcn.tm.config.TxManagerConfig;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import com.codingapi.txlcn.tm.core.storage.LockValue;
import com.codingapi.txlcn.tm.core.storage.TransactionUnit;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

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

    private static final String REDIS_MACHINE_ID_MAP_PREFIX = "tm.machine.id.gen:";

    private RedisTemplate<String, Object> redisTemplate;

    private StringRedisTemplate stringRedisTemplate;

    private TxManagerConfig managerConfig;

    public RedisStorage() {
    }

    public RedisStorage(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate,
                        TxManagerConfig managerConfig) {
        this.redisTemplate = redisTemplate;
        this.managerConfig = managerConfig;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void initGroup(String groupId) {
        redisTemplate.opsForHash().put(REDIS_GROUP_PREFIX + groupId, "root", "");
        redisTemplate.expire(REDIS_GROUP_PREFIX + groupId, managerConfig.getDtxTime() + 10000, TimeUnit.MILLISECONDS);
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
        if (Objects.isNull(locks) || locks.isEmpty()) {
            return;
        }
        Map<String, LockValue> lockIds = locks.stream().collect(Collectors.toMap(lock -> contextId + lock, lock -> lockValue));
        String firstLockId = contextId + new ArrayList<>(locks).get(0);
        Boolean result = redisTemplate.opsForValue().multiSetIfAbsent(lockIds);
        if (!Optional.ofNullable(result).orElse(false)) {
            LockValue hasLockValue = (LockValue) redisTemplate.opsForValue().get(firstLockId);
            if (Objects.isNull(hasLockValue)) {
                throw new FastStorageException("acquire locks fail.", FastStorageException.EX_CODE_REPEAT_LOCK);
            }
            // 不在同一个DTX下，已存在的锁是排它锁 或者 新请求的不是共享锁时， 获取锁失败
            if (Objects.isNull(lockValue.getGroupId()) || !lockValue.getGroupId().equals(hasLockValue.getGroupId())) {
                if (hasLockValue.getLockType() == DTXLocks.X_LOCK || lockValue.getLockType() != DTXLocks.S_LOCK) {
                    throw new FastStorageException("acquire locks fail.", FastStorageException.EX_CODE_REPEAT_LOCK);
                }
            }
            redisTemplate.opsForValue().multiSet(lockIds);
        }

        // 锁超时时间设置
        lockIds.forEach((k, v) -> redisTemplate.expire(k, managerConfig.getDtxTime(), TimeUnit.MILLISECONDS));
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
        stringRedisTemplate.opsForHash().put(REDIS_TM_LIST,
                tmProperties.getHost() + ":" + tmProperties.getTransactionPort(), String.valueOf(tmProperties.getHttpPort()));
    }

    @Override
    public List<TMProperties> findTMProperties() {
        return stringRedisTemplate.opsForHash().entries(REDIS_TM_LIST).entrySet().stream()
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

    private static final String GLOBAL_CONTEXT = "root";
    private static final String GLOBAL_LOCK_ID = "global.lock";

    private void acquireGlobalXLock() {
        LockValue lockValue = new LockValue();
        lockValue.setLockType(DTXLocks.X_LOCK);
        while (true) {
            try {
                acquireLocks(GLOBAL_CONTEXT, Sets.newHashSet(GLOBAL_LOCK_ID), lockValue);
                break;
            } catch (FastStorageException ignored) {
            }
        }
    }

    private void releaseGlobalXLock() {
        releaseLocks(GLOBAL_CONTEXT, Sets.newHashSet(GLOBAL_LOCK_ID));
    }

    @Override
    public long acquireMachineId(long machineMaxSize, long timeout) throws FastStorageException {
        try {
            acquireGlobalXLock();
            stringRedisTemplate.opsForValue().setIfAbsent(REDIS_MACHINE_ID_MAP_PREFIX + "cur_id", "-1");
            for (int i = 0; i < machineMaxSize; i++) {
                long curId = Objects.requireNonNull(
                        stringRedisTemplate.opsForValue().increment(REDIS_MACHINE_ID_MAP_PREFIX + "cur_id", 1));
                if (curId > machineMaxSize) {
                    stringRedisTemplate.opsForValue().set(REDIS_MACHINE_ID_MAP_PREFIX + "cur_id", "0");
                    curId = 0;
                }
                if (Optional
                        .ofNullable(stringRedisTemplate.hasKey(REDIS_MACHINE_ID_MAP_PREFIX + curId))
                        .orElse(true)) {
                    continue;
                }
                stringRedisTemplate.opsForValue().set(REDIS_MACHINE_ID_MAP_PREFIX + curId, "", timeout, TimeUnit.MILLISECONDS);
                return curId;
            }
            throw new FastStorageException("non can used machine id", FastStorageException.EX_CODE_NON_MACHINE_ID);
        } finally {
            releaseGlobalXLock();
        }
    }

    @Override
    public void refreshMachines(long timeout, long... machines) {
        try {
            stringRedisTemplate.setEnableTransactionSupport(true);
            stringRedisTemplate.multi();
            for (long mac : machines) {
                stringRedisTemplate.opsForValue().set(REDIS_MACHINE_ID_MAP_PREFIX + mac, "", timeout, TimeUnit.MILLISECONDS);
            }
            stringRedisTemplate.exec();
        } catch (Throwable e) {
            stringRedisTemplate.discard();
        } finally {
            stringRedisTemplate.setEnableTransactionSupport(false);
        }
    }
}
