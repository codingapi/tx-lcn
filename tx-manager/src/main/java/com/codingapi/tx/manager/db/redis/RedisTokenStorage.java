package com.codingapi.tx.manager.db.redis;

import com.codingapi.tx.manager.restapi.auth.sauth.token.TokenStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Date: 2018/12/28
 *
 * @author ujued
 */
@Component
public class RedisTokenStorage implements TokenStorage {

    private static final String REDIS_PREFIX = "tx.manager:token";

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisTokenStorage(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean exist(String token) {
        Long size = redisTemplate.opsForList().size(REDIS_PREFIX);
        if (Objects.isNull(size)) {
            return false;
        }
        List<String> tokens = redisTemplate.opsForList().range(REDIS_PREFIX, 0, size);
        return tokens.contains(token);
    }

    @Override
    public void add(String token) {
        Objects.requireNonNull(token);
        Long size = redisTemplate.opsForList().size(REDIS_PREFIX);
        redisTemplate.opsForList().leftPush(REDIS_PREFIX, token);
        redisTemplate.expire(REDIS_PREFIX, 20, TimeUnit.MINUTES);
        if (Objects.nonNull(size) && size > 3) {
            redisTemplate.opsForList().rightPop(REDIS_PREFIX);
        }
    }

    @Override
    public void remove(String token) {
        throw new IllegalStateException("not support");
    }

    @Override
    public void clear() {
        redisTemplate.delete(REDIS_PREFIX);
    }
}
