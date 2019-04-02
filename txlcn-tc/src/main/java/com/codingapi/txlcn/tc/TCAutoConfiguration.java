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

import com.codingapi.txlcn.common.exception.TxClientException;
import com.codingapi.txlcn.common.runner.TxLcnApplicationRunner;
import com.codingapi.txlcn.common.util.ApplicationInformation;
import com.codingapi.txlcn.common.util.id.ModIdProvider;
import com.codingapi.txlcn.logger.TxLoggerConfiguration;
import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import com.codingapi.txlcn.tc.config.TxClientConfig;
import com.codingapi.txlcn.tracing.TracingAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Date: 1/19/19
 *
 * @author ujued
 * @see EnableDistributedTransaction
 */
@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASPECTJ, pattern = "com.codingapi.txlcn.tc.core.transaction.txc..*"
        )
)
@Import({TxLoggerConfiguration.class, TracingAutoConfiguration.class})
public class TCAutoConfiguration {

    /**
     * All initialization about TX-LCN
     *
     * @param applicationContext Spring ApplicationContext
     * @return TX-LCN custom runner
     */
    @Bean
    public ApplicationRunner txLcnApplicationRunner(ApplicationContext applicationContext) {
        return new TxLcnApplicationRunner(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public ModIdProvider modIdProvider(ConfigurableEnvironment environment,
                                       @Autowired(required = false) ServerProperties serverProperties) {
        return () -> ApplicationInformation.modId(environment, serverProperties);
    }

    @Bean
    @ConfigurationProperties(prefix = "tx-lcn.client")
    public TxClientConfig txClientConfig(@Autowired StringRedisTemplate stringRedisTemplate) throws TxClientException {
        TxClientConfig txClientConfig = new TxClientConfig();

        String REDIS_TM_LIST = "tm.instances";

        List<String> managerAddress = stringRedisTemplate.opsForHash().entries(REDIS_TM_LIST).entrySet().stream()
                .map(entry -> entry.getKey().toString()).collect(Collectors.toList());

        if(CollectionUtils.isEmpty(managerAddress)){
            throw new TxClientException("在redis没有找到可用的tx-manager地址");
        }

        txClientConfig.setManagerAddress(managerAddress);

        return txClientConfig;
    }
}
