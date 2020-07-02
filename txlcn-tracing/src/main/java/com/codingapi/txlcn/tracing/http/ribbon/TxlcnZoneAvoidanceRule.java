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

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tracing.TracingContext;
import com.codingapi.txlcn.tracing.spring.SpringConfig;
import com.codingapi.txlcn.tracing.spring.TracingSpringContextUtils;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.serviceregistry.Registration;

import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Slf4j
public class TxlcnZoneAvoidanceRule extends ZoneAvoidanceRule {

    private final Registration registration;

    /**
     * 无参构造器提供给Ribbon调用
     */
    public TxlcnZoneAvoidanceRule() {
        this.registration = null;
    }

    TxlcnZoneAvoidanceRule(Registration registration) {
        this.registration = registration;
    }

    @Override
    public Server choose(Object key) {
        // 0. 非分布式事务直接执行默认业务.
        if (!TracingContext.tracing().hasGroup()) {
            return super.choose(key);
        }

        // 1. 自己加入此事务组调用链
        assert Objects.nonNull(registration);
        //兼容springBoot 2.0以下版本
        Boolean exitHost;
        try {
            exitHost = registration.getHost() != null;
        } catch (NoSuchMethodError noSuchMethodError) {
            exitHost = false;
        }
        if (!exitHost) {
            SpringConfig springConfig = (SpringConfig) TracingSpringContextUtils.getContext().getBean("springConfig");
            TracingContext.tracing().addApp(registration.getServiceId(), springConfig.getHost() + ":" + springConfig.getPort());
        } else {
            TracingContext.tracing().addApp(registration.getServiceId(), registration.getHost() + ":" + registration.getPort());
        }

        // 2. 获取所有要访问服务的实例
        List<Server> servers = getLoadBalancer().getAllServers();
        assert !servers.isEmpty();

        JSONObject appMap = TracingContext.tracing().appMap();
        log.debug("load balanced rule servers: {}, txGroup[{}]'s server map:{}",
                servers, TracingContext.tracing().groupId(), appMap);
        Server balanceServer = null;
        String serviceId = servers.get(0).getMetaInfo().getAppName();
        if (appMap.containsKey(serviceId)) {
            for (Server server : servers) {
                if (server.getHostPort().equals(appMap.getString(serviceId))) {
                    log.debug("txlcn chosen server [{}] in txGroup: {}", server, TracingContext.tracing().groupId());
                    balanceServer = server;
                }
            }
        }
        if (Objects.isNull(balanceServer)) {
            Server server = super.choose(key);
            TracingContext.tracing().addApp(server.getMetaInfo().getAppName(), server.getHostPort());
            return server;
        }
        return balanceServer;
    }
}
