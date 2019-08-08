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
package com.codingapi.txlcn.tc;

import com.codingapi.txlcn.common.base.Consts;
import com.codingapi.txlcn.common.runner.TxLcnInitializer;
import com.codingapi.txlcn.common.util.ApplicationInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Description: Date: 1/24/19
 *
 * @author codingapi
 */
@Component
@Slf4j
public class TCAutoCluster implements TxLcnInitializer {

    private InetUtils inet;

    private final ServerProperties serverProperties;

    private final RedisTemplate<String, String> stringRedisTemplate;

    private String labelName;

    @Autowired
    public TCAutoCluster(InetUtils inet, ServerProperties serverProperties, RedisTemplate<String, String> stringRedisTemplate) {
        this.inet = inet;
        this.serverProperties = serverProperties;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void init() throws Exception {
        labelName = inet.findFirstNonLoopbackHostInfo().getIpAddress() + ":" + ApplicationInformation.serverPort(serverProperties);
        stringRedisTemplate.opsForSet().add(Consts.REDIS_TC_LIST, labelName);
        log.info("save the tc-instances to redis: {}", labelName);
    }

    @Override
    public void destroy() throws Exception {
        log.info("remove the tc-instances from redis: {}", labelName);
        stringRedisTemplate.opsForSet().remove(Consts.REDIS_TC_LIST, labelName);
    }
}
