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
package com.codingapi.txlcn.manager;

import com.codingapi.txlcn.commons.runner.TxLcnApplicationRunner;
import com.codingapi.txlcn.logger.TxLoggerConfiguration;
import com.codingapi.txlcn.manager.banner.TxLcnManagerBanner;
import com.codingapi.txlcn.spi.MessageConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
public class TxManagerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(TxManagerApplication.class);
        springApplication.setBanner(new TxLcnManagerBanner());
        springApplication.run(args);
    }

    @Bean
    public ExecutorService executorService() {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdown();
            try {
                executorService.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException ignored) {
            }
        }));
        return executorService;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public TxLcnApplicationRunner txLcnApplicationRunner(ApplicationContext applicationContext) {
        return new TxLcnApplicationRunner(applicationContext);
    }

}
