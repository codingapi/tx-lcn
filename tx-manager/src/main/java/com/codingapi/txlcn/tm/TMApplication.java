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
package com.codingapi.txlcn.tm;

import com.codingapi.txlcn.commons.runner.TxLcnApplicationRunner;
import com.codingapi.txlcn.logger.TxLoggerConfiguration;
import com.codingapi.txlcn.tm.banner.TxLcnManagerBanner;
import com.codingapi.txlcn.tm.config.TxManagerConfig;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import com.codingapi.txlcn.tm.core.storage.FastStorageProvider;
import com.codingapi.txlcn.tm.core.storage.redis.RedisStorage;
import com.codingapi.txlcn.spi.MessageConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/11/29
 *
 * @author lorne
 */
@SpringBootApplication
@Import({TxLoggerConfiguration.class, MessageConfiguration.class})
public class TMApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(TMApplication.class);
        springApplication.setBanner(new TxLcnManagerBanner());
        springApplication.run(args);
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService executorService() {
        int coreSize = Runtime.getRuntime().availableProcessors() * 2;
        return new ThreadPoolExecutor(coreSize, coreSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()) {
            @Override
            public void shutdown() {
                super.shutdown();
                try {
                    this.awaitTermination(10, TimeUnit.MINUTES);
                } catch (InterruptedException ignored) {
                }
            }
        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public FastStorageProvider fastStorageProvider(RedisTemplate<String, Object> redisTemplate, TxManagerConfig managerConfig) {
        return () -> new RedisStorage(redisTemplate, managerConfig);
    }

    @Bean
    public FastStorage fastStorage(FastStorageProvider fastStorageProvider) {
        return fastStorageProvider.provide();
    }

    @Bean
    public TxLcnApplicationRunner txLcnApplicationRunner(ApplicationContext applicationContext) {
        return new TxLcnApplicationRunner(applicationContext);
    }

}
