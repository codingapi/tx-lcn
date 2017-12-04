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

	@Bean
	public ILoadBalancer ribbonLoadBalancer(IClientConfig config,
			ServerList<Server> serverList, ServerListFilter<Server> serverListFilter,
			IRule rule, IPing ping, ServerListUpdater serverListUpdater) {
		return new LcnZoneAwareLoadBalancerProxy<>(config, rule, ping, serverList,
				serverListFilter, serverListUpdater);
	}
	
	/*@Bean
	public IRule ribbonRule(){
		return new LcnRandomRuleProxy();
	}*/

}
