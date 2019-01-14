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
package com.codingapi.txlcn.spi.message.netty;

import com.codingapi.txlcn.spi.message.netty.bean.RpcCmdContext;
import com.codingapi.txlcn.spi.message.ClientInitCallBack;
import com.codingapi.txlcn.spi.message.RpcAnswer;
import com.codingapi.txlcn.spi.message.loadbalance.RpcLoadBalance;
import com.codingapi.txlcn.spi.message.netty.loadbalance.RandomLoadBalance;
import com.codingapi.txlcn.spi.message.RpcConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

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
public class RpcNettyConfiguration {


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
        return remoteKey -> log.info("connected->{}", remoteKey);
    }

    @PostConstruct
    public void init() {
        RpcCmdContext.getInstance().setWaitTime(rpcConfig().getWaitTime());
        RpcCmdContext.getInstance().setCacheSize(rpcConfig().getCacheSize());
        SocketManager.getInstance().setAttrDelayTime(rpcConfig().getAttrDelayTime());
    }
}
