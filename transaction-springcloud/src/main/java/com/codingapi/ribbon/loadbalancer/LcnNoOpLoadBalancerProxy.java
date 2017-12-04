package com.codingapi.ribbon.loadbalancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.loadbalancer.NoOpLoadBalancer;
import com.netflix.loadbalancer.Server;

public class LcnNoOpLoadBalancerProxy extends NoOpLoadBalancer {
	
	private Logger logger = LoggerFactory.getLogger(LcnNoOpLoadBalancerProxy.class);
	
	LcnLoadBalancerRule lcnLoadBalancerRule = new LcnLoadBalancerRule();
	
	public LcnNoOpLoadBalancerProxy(){
		super();
	}

	@Override
	public Server chooseServer(Object key){
		logger.info("enter chooseServer method, key:" + key);
		return lcnLoadBalancerRule.proxy(super.chooseServer(key));
	}

}
