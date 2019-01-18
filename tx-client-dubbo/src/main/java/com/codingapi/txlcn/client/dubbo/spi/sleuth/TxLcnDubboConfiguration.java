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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/14
 *
 * @author ujued
 */
@Configuration
@ComponentScan
public class TxLcnDubboConfiguration {
    
    static {
        System.setProperty("dubbo.provider.filter","tracing");
        System.setProperty("dubbo.consumer.filter","tracing");
    }
    
    @Bean
    public TxLcnDubboInitializer txLcnDubboInitializer(SleuthParamListener sleuthParamListener) {
        return new TxLcnDubboInitializer(sleuthParamListener);
    }
    
}
