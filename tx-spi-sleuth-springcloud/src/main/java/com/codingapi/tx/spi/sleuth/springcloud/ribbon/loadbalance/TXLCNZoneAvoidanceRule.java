package com.codingapi.tx.spi.sleuth.springcloud.ribbon.loadbalance;

import com.codingapi.tx.spi.sleuth.TracerHelper;
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

    private final SleuthParamListener sleuthParamListener;

    private final Registration registration;

    private final TracerHelper tracerHelper;

    public TXLCNZoneAvoidanceRule() {
        this(null, null, null);
    }

    public TXLCNZoneAvoidanceRule(SleuthParamListener sleuthParamListener,
                                  Registration registration,
                                  TracerHelper tracerHelper) {
        this.sleuthParamListener = sleuthParamListener;
        this.registration = registration;
        this.tracerHelper = tracerHelper;
    }

    @Override
    public Server choose(Object key) {
        return getServer(key);
    }

    private Server getServer(Object key) {
        log.debug("load balanced rule key: {}, app list: {}", key, tracerHelper.getAppList());
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
            sleuthParamListener.alfterNewBalance(String.format("%s:%s", server.getMetaInfo().getAppName(), server.getHostPort()));
            log.debug("app list: {}", tracerHelper.getAppList());
            return server;
        } else {
            log.debug("app list hit: {}", tracerHelper.getAppList());
            return balanceServer;
        }
    }


}
