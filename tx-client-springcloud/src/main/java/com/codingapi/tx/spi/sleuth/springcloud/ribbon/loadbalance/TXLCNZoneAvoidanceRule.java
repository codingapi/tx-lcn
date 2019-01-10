package com.codingapi.tx.spi.sleuth.springcloud.ribbon.loadbalance;

import com.codingapi.tx.spi.sleuth.listener.SleuthParamListener;
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
