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
package com.codingapi.txlcn.spi;

import com.codingapi.txlcn.spi.message.listener.ClientInitCallBack;
import com.codingapi.txlcn.spi.message.RpcAnswer;
import com.codingapi.txlcn.spi.message.RpcConfig;
import com.codingapi.txlcn.spi.message.listener.DefaultClientInitCallback;
import com.codingapi.txlcn.spi.message.listener.DefaultTCRegisterListener;
import com.codingapi.txlcn.spi.message.listener.TCRegisterListener;
import com.codingapi.txlcn.spi.message.loadbalance.RpcLoadBalance;
import com.codingapi.txlcn.spi.message.netty.loadbalance.RandomLoadBalance;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Configuration
@ComponentScan
@Slf4j
@Data
public class MessageConfiguration {


    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("tx-lcn.message.netty")
    public RpcConfig rpcConfig() {
        return new RpcConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcAnswer rpcClientAnswer() {
        return rpcCmd -> log.info("cmd->{}", rpcCmd);
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcLoadBalance rpcLoadBalance() {
        return new RandomLoadBalance();
    }


    @Bean
    @ConditionalOnMissingBean
    public ClientInitCallBack clientInitCallBack() {
        return new DefaultClientInitCallback();
    }

    @Bean
    @ConditionalOnMissingBean
    public TCRegisterListener tcRegisterListener() {
        return new DefaultTCRegisterListener();
    }
}
