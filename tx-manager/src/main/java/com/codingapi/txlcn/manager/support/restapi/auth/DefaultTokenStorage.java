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
