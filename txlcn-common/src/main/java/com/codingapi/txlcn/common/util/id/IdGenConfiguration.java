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
package com.codingapi.txlcn.common.util.id;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Date: 19-1-30 下午8:53
 *
 * @author ujued
 */
@Configuration
@ComponentScan
@ConfigurationProperties("tx-lcn.common.id")
@EnableConfigurationProperties(IdGenConfiguration.class)
@Data
public class IdGenConfiguration {

    private long idcId;

    private long machineId;

    @Bean
    @ConditionalOnMissingBean
    public IdGen idGen() {
        SnowFlakeGenerator.Factory factory = new SnowFlakeGenerator.Factory(15, 0);
        SnowFlakeGenerator snowFlakeGenerator = factory.create(idcId, machineId);
        return snowFlakeGenerator::nextId;
    }
}
