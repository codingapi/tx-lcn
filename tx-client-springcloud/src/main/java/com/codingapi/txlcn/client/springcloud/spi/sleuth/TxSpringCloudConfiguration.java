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
package com.codingapi.txlcn.client.springcloud.spi.sleuth;

import com.codingapi.txlcn.spi.sleuth.listener.SleuthParamListener;
import com.codingapi.txlcn.client.springcloud.spi.sleuth.ribbon.loadbalance.TXLCNZoneAvoidanceRule;
import com.netflix.loadbalancer.IRule;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Data
@ComponentScan
@Configuration
@ConfigurationProperties(prefix = "tx-lcn.springcloud.loadbalance")
public class TxSpringCloudConfiguration {

    private boolean enabled = false;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "tx-lcn.springcloud.loadbalance.enabled",havingValue = "true")
    public IRule ribbonRule(SleuthParamListener sleuthParamListener,
                            Registration registration){
        return new TXLCNZoneAvoidanceRule(sleuthParamListener, registration);
    }
}
