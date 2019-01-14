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
package com.codingapi.txlcn.client.dubbo.spi.sleuth;

import com.codingapi.txlcn.spi.sleuth.listener.SleuthParamListener;
import com.codingapi.txlcn.client.dubbo.spi.sleuth.loadbalance.TXLCNLoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/14
 *
 * @author ujued
 */
@Configuration
@ComponentScan
@Slf4j
public class TxDubboConfiguration {

    static {
        System.setProperty("dubbo.provider.filter","tracing");
        System.setProperty("dubbo.consumer.filter","tracing");
    }

    @Autowired
    private SleuthParamListener sleuthParamListener;

    @PostConstruct
    public void init(){
        TXLCNLoadBalance.sleuthParamListener = sleuthParamListener;
        log.info("init sleuthParamListener->{}",sleuthParamListener);
    }
}
