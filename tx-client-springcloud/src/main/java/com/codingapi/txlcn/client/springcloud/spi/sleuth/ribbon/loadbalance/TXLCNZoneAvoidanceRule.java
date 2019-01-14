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
package com.codingapi.txlcn.client.springcloud.spi.sleuth.ribbon.loadbalance;

import com.codingapi.txlcn.spi.sleuth.listener.SleuthParamListener;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.Scope;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Slf4j
@Scope("prototype")
public class TXLCNZoneAvoidanceRule extends ZoneAvoidanceRule {

    //针对sleuth 负载控制的ExtraField参数设置
    private final SleuthParamListener sleuthParamListener;

    private final Registration registration;

    public TXLCNZoneAvoidanceRule(SleuthParamListener sleuthParamListener,
                                  Registration registration) {
        this.sleuthParamListener = sleuthParamListener;
        this.registration = registration;
    }

    @Override
    public Server choose(Object key) {
        return getServer(key);
    }

    private Server getServer(Object key) {
        String localKey = String.format("%s:%s:%s", registration.getServiceId(), registration.getHost(), registration.getPort());
        List<String> appList = sleuthParamListener.beforeBalance(localKey);
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
            sleuthParamListener.afterNewBalance(String.format("%s:%s", server.getMetaInfo().getAppName(), server.getHostPort()));
            return server;
        } else {
            return balanceServer;
        }
    }

}
