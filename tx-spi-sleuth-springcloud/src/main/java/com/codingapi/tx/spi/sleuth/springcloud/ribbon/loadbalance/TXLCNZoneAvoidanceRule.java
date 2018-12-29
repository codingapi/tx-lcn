package com.codingapi.tx.spi.sleuth.springcloud.ribbon.loadbalance;

import com.codingapi.tx.spi.sleuth.TracerHelper;
import com.codingapi.tx.spi.sleuth.listener.SleuthParamListener;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Slf4j
@Component
@Scope("prototype")
public class TXLCNZoneAvoidanceRule extends ZoneAvoidanceRule {

    @Autowired
    private SleuthParamListener sleuthParamListener;

    @Autowired
    private Registration registration;

    @Autowired
    private TracerHelper tracerHelper;
    @Override
    public Server choose(Object key) {
        return getServer(key);
    }

    private Server getServer(Object key) {
        log.info("load balanced rule key: {}, applist: {}", key, tracerHelper.getAppList());
        String localKey = String.format("%s:%s:%s",registration.getServiceId(),registration.getHost(),registration.getPort());
        List<String> appList = sleuthParamListener.beforeBalance(localKey);
        Server balanceServer = null;
        List<Server> servers = getLoadBalancer().getAllServers();
        log.info("load balanced rule servers: {}", servers);
        for (Server server : servers) {
            for (String appKey : appList) {
                String serverKey = String.format("%s:%s",server.getMetaInfo().getAppName(),server.getHostPort());
                if (serverKey.equals(appKey)) {
                    balanceServer = server;
                }
            }
        }
        if(balanceServer==null) {
            Server server = super.choose(key);
            sleuthParamListener.alfterNewBalance(String.format("%s:%s",server.getMetaInfo().getAppName(),server.getHostPort()));
            log.info("Applist: {}", tracerHelper.getAppList());
            return server;
        }else {
            log.info("Applist hit: {}", tracerHelper.getAppList());
            return balanceServer;
        }
    }


}
