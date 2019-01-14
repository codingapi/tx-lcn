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

import com.codingapi.txlcn.manager.config.TxManagerConfig;
import com.codingapi.txlcn.manager.db.ManagerStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manager信息改用Set 暂未使用
 *
 * @author meetzy
 */
@Slf4j
public class RedisSetManagerStorage implements ManagerStorage {
    
    private static final String REDIS_PREFIX = "tx.manager.list";
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Autowired
    private TxManagerConfig managerConfig;
    
    @Value("${server.port}")
    private int port;
    
    @Override
    public List<String> addressList() {
        return new ArrayList<>(Objects.requireNonNull(redisTemplate.opsForSet().members(REDIS_PREFIX)));
    }
    
    @Override
    public void remove(String address) {
        redisTemplate.opsForSet().remove(REDIS_PREFIX, address);
    }
    
    @PostConstruct
    public void init() {
        String address = managerConfig.getHost() + ":" + port;
        redisTemplate.opsForSet().add(REDIS_PREFIX, address);
        log.info("manager add redis finish.");
    }
    
    @PreDestroy
    public void destroy() {
        String address = managerConfig.getHost() + ":" + port;
        remove(address);
        log.info("manager remove redis.");
    }
}
