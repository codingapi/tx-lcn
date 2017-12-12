package com.codingapi.ribbon.loadbalancer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.netflix.loadbalancer.ServerListFilter;
import com.netflix.loadbalancer.ServerListUpdater;

@Configuration
public class LcnRibbonConfiguration {

	/**
	 * 为ribbon的loadbalancer做代理，相比于重写IRULE,重写loadbalancer更有利于用户自选LB算法，而且有默认LB算法可用
	 * @param config
	 * @param serverList
	 * @param serverListFilter
	 * @param rule
	 * @param ping
	 * @param serverListUpdater
	 * @return
	 */
	@Bean
	public ILoadBalancer ribbonLoadBalancer(IClientConfig config,
			ServerList<Server> serverList, ServerListFilter<Server> serverListFilter,
			IRule rule, IPing ping, ServerListUpdater serverListUpdater) {
		return new LcnZoneAwareLoadBalancerProxy(config, rule, ping, serverList,
				serverListFilter, serverListUpdater);
	}


}
