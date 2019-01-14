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

import com.codingapi.txlcn.manager.support.restapi.auth.sauth.token.TokenStorage;
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
