package com.codingapi.tx.manager.restapi.auth;

import com.codingapi.tx.manager.db.redis.RedisTokenStorage;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 2018/12/28
 *
 * @author ujued
 */
@Component
@Primary
public class DefaultTokenStorage extends RedisTokenStorage {

    public DefaultTokenStorage(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }
}
