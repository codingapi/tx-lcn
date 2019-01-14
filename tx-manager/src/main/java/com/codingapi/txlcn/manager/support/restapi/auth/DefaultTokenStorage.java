package com.codingapi.txlcn.manager.support.restapi.auth;

import com.codingapi.txlcn.manager.db.redis.RedisTokenStorage;
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
