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
package com.codingapi.txlcn.tracing.http.ribbon;

import com.netflix.loadbalancer.IRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Description:
 * Date: 19-1-29 上午9:37
 *
 * @author ujued
 */
@Configuration
@ConditionalOnClass(IRule.class)
public class LoadbalancerConfiguration {

    @Bean
    @ConditionalOnProperty(name = "tx-lcn.ribbon.loadbalancer.dtx.enabled", matchIfMissing = true)
    @Scope("prototype")
    public IRule ribbonRule(Registration registration) {
        return new TxlcnZoneAvoidanceRule(registration);
    }
}
