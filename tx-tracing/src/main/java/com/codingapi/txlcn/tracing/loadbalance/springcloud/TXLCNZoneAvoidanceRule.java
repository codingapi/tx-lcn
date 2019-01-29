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
package com.codingapi.txlcn.tracing.loadbalance.springcloud;

import com.codingapi.txlcn.tracing.TracingContext;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.serviceregistry.Registration;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Slf4j
public class TXLCNZoneAvoidanceRule extends ZoneAvoidanceRule {

    private final Registration registration;

    /**
     * 无参构造器提供给Ribbon调用
     */
    public TXLCNZoneAvoidanceRule() {
        this.registration = null;
    }

    public TXLCNZoneAvoidanceRule(Registration registration) {
        this.registration = registration;
    }

    @Override
    public Server choose(Object key) {
        return getServer(key);
    }

    private Server getServer(Object key) {
        assert registration != null;
        List<String> appList = TracingContext.tracingContext().appList();
        Server balanceServer = null;
        List<Server> servers = getLoadBalancer().getAllServers();
        log.debug("load balanced rule servers: {}", servers);
        for (Server server : servers) {
            for (String appKey : appList) {
                String serverKey = String.format("%s:%s", server.getMetaInfo().getAppName(), server.getHostPort());
                if (serverKey.equals(appKey)) {
                    balanceServer = server;
                }
            }
        }
        if (balanceServer == null) {
            Server server = super.choose(key);
            TracingContext.tracingContext().addApp(String.format("%s:%s", server.getMetaInfo().getAppName(), server.getHostPort()));
            return server;
        } else {
            return balanceServer;
        }
    }

}
