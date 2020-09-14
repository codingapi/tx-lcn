package com.codingapi.txlcn.tm.repository.redis;

import com.codingapi.txlcn.tm.repository.TmNodeInfo;
import com.codingapi.txlcn.tm.repository.TmNodeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020/9/3 17:49
 */
@Slf4j
@AllArgsConstructor
public class RedisTmNodeRepository implements TmNodeRepository {

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取符合条件的key
     *
     * @param pattern 表达式
     */
    @Override
    public List<String> keys(String pattern) {
        List<String> keys = new ArrayList<>();
        this.scan(pattern, item -> {
            //符合条件的key
            String key = new String(item, StandardCharsets.UTF_8);
            keys.add(key);
        });
        return keys;
    }

    /**
     * scan 实现
     *
     * @param pattern  表达式
     * @param consumer 对迭代到的key进行操作
     */
    private void scan(String pattern, Consumer<byte[]> consumer) {
        this.redisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection
                    .scan(ScanOptions.scanOptions()
                            .count(Long.MAX_VALUE)
                            .match(pattern)
                            .build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 获取 Tm 节点的ip地址
     *
     * @param key Tm 全局唯一 ID
     */
    @Override
    public TmNodeInfo getTmNodeInfo(String key) {
        return (TmNodeInfo) redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Boolean setIfAbsent(TmNodeInfo tmNodeInfo, long expireTime) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.setIfAbsent(tmNodeInfo.getTmId(), tmNodeInfo, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public void set(TmNodeInfo tmNodeInfo, long expireTime) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(tmNodeInfo.getTmId(), tmNodeInfo, expireTime, TimeUnit.SECONDS);
    }
}
