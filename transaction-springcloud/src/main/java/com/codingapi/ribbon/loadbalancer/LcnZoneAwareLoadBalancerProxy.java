package com.codingapi.ribbon.loadbalancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.netflix.loadbalancer.ServerListFilter;
import com.netflix.loadbalancer.ServerListUpdater;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;

/**
 * created by foxdd 2017-12-05
 */
public class LcnZoneAwareLoadBalancerProxy<T extends Server> extends ZoneAwareLoadBalancer<T> {
	
	private Logger logger = LoggerFactory.getLogger(LcnZoneAwareLoadBalancerProxy.class);
	
	LcnLoadBalancerRule lcnLoadBalancerRule = new LcnLoadBalancerRule();
	
	public LcnZoneAwareLoadBalancerProxy(IClientConfig clientConfig, IRule rule,
            IPing ping, ServerList<T> serverList, ServerListFilter<T> filter,
            ServerListUpdater serverListUpdater) {
		super(clientConfig, rule, ping, serverList, filter, serverListUpdater);
	}

	@Override
	public Server chooseServer(Object key){
		logger.info("enter chooseServer method, key:" + key);
		return lcnLoadBalancerRule.proxy(getAllServers(),super.chooseServer(key));
	}

}
