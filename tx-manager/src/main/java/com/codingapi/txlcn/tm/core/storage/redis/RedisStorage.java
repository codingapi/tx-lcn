package com.codingapi.txlcn.tm.core.storage.redis;

import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tm.config.TxManagerConfig;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import com.codingapi.txlcn.tm.core.storage.FastStorageException;
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

    private static final String REDIS_PREFIX = "tm:group:";

    private static final String REDIS_GROUP_STATE = REDIS_PREFIX + "transactionState:";

    private static final String REDIS_TOKEN_PREFIX = "tm.token";

    private static final String REDIS_TM_LIST = "tm.instances";

    private RedisTemplate<String, Object> redisTemplate;

    private TxManagerConfig managerConfig;

    public RedisStorage() {
    }

    public RedisStorage(RedisTemplate<String, Object> redisTemplate, TxManagerConfig managerConfig) {
        this.redisTemplate = redisTemplate;
        this.managerConfig = managerConfig;
    }

    @Override
    public void initGroup(String groupId) {
        redisTemplate.opsForHash().put(REDIS_PREFIX + groupId, "root", "non");
        redisTemplate.expire(REDIS_PREFIX + groupId, managerConfig.getDtxTime() + 10000, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean containsTransactionUnit(String groupId, TransactionUnit transactionUnit) {
        Object unit = redisTemplate.opsForHash().get(REDIS_PREFIX + groupId, transactionUnit.getUnitId());
        return Objects.nonNull(unit);
    }

    @Override
    public boolean containsGroup(String groupId) {
        return Optional.ofNullable(redisTemplate.hasKey(REDIS_PREFIX + groupId)).orElse(false);
    }

    @Override
    public List<TransactionUnit> findTransactionUnitsFromGroup(String groupId) throws FastStorageException {
        Map<Object, Object> units = redisTemplate.opsForHash().entries(REDIS_PREFIX + groupId);
        log.debug("transaction units: {}", units);
        return units.entrySet().stream()
                .filter(objectObjectEntry -> !objectObjectEntry.getKey().equals("root"))
                .map(objectObjectEntry -> (TransactionUnit) objectObjectEntry.getValue()).collect(Collectors.toList());
    }

    @Override
    public void saveTransactionUnitToGroup(String groupId, TransactionUnit transactionUnit) throws FastStorageException {
        if (Optional.ofNullable(redisTemplate.hasKey(REDIS_PREFIX + groupId)).orElse(false)) {
            redisTemplate.opsForHash().put(REDIS_PREFIX + groupId, transactionUnit.getUnitId(), transactionUnit);
            return;
        }
        throw new FastStorageException("attempts to the non-existent transaction group " + groupId,
                FastStorageException.EX_CODE_NON_GROUP);
    }

    @Override
    public void clearGroup(String groupId) {
        log.debug("remove group:{} from redis.", groupId);
        redisTemplate.delete(REDIS_PREFIX + groupId);
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
            return 0;
        }
    }

    @Override
    public void acquireLock(String cate, String key) throws FastStorageException {
        String globalLockId = cate + key;
        redisTemplate.setEnableTransactionSupport(true);
        try {
            redisTemplate.multi();
            if (Optional.ofNullable(redisTemplate.hasKey(globalLockId)).orElse(true)) {
                // has lock
                throw new FastStorageException("repeat lock", FastStorageException.EX_CODE_REPEAT_LOCK);
            }
            redisTemplate.opsForValue().set(globalLockId, Boolean.TRUE, managerConfig.getDtxLockTime(), TimeUnit.MILLISECONDS);
            redisTemplate.exec();
        } catch (Exception e) {
            redisTemplate.discard();
        } finally {
            redisTemplate.setEnableTransactionSupport(false);
        }
    }

    @Override
    public void releaseLock(String cate, String key) {
        redisTemplate.delete(cate + key);
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
    public void saveTMAddress(String address) {
        Objects.requireNonNull(address);
        doInTransaction(address, ((key, list) -> list.add(key)));
    }

    @Override
    public List<String> findTMAddresses() {
        return JSON.parseArray(Optional.ofNullable(redisTemplate.opsForValue().get(REDIS_TM_LIST)).orElse("[]").toString()).stream()
                .filter(address -> !address.equals(managerConfig.getHost() + ":" + managerConfig.getPort()))
                .map(Object::toString).collect(Collectors.toList());
    }

    @Override
    public void removeTMAddress(String address) {
        Objects.requireNonNull(address);
        doInTransaction(address, ((key, list) -> list.remove(key)));
    }

    private void doInTransaction(String address, ListOperation operation) {
        redisTemplate.setEnableTransactionSupport(true);
        try {
            redisTemplate.multi();
            List<String> addressList = findTMAddresses();
            operation.exec(address, addressList);
            redisTemplate.opsForValue().set(REDIS_TM_LIST, JSON.toJSONString(addressList));
            redisTemplate.exec();
        } catch (Exception e) {
            redisTemplate.discard();
        } finally {
            redisTemplate.setEnableTransactionSupport(false);
        }
    }

    public interface ListOperation {
        void exec(String key, List<String> list);
    }
}
