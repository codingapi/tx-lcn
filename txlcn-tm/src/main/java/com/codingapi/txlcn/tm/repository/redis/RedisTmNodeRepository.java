package com.codingapi.txlcn.tm.repository.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020/9/3 17:49
 */
@Slf4j
@Component
public class RedisTmNodeRepository {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取符合条件的key
     *
     * @param pattern 表达式
     */
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
        this.stringRedisTemplate.execute((RedisConnection connection) -> {
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
}
