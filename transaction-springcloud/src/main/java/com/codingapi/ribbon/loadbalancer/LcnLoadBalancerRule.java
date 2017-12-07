package com.codingapi.ribbon.loadbalancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codingapi.tx.Constants;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.netflix.loadbalancer.Server;

/**
 * created by foxdd 2017-12-05
 */
public class LcnLoadBalancerRule {
	
	private Logger logger = LoggerFactory.getLogger(LcnLoadBalancerRule.class);
	
	public Server proxy(Server server){
		logger.info("LCNloadBalancer proxy -> map-size -> " + Constants.cacheModelInfo.size());
		logger.info("The selected server info, host:" + server.getHost() + ", port:" + server.getPort());
		TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
		if(txTransactionLocal == null){
			return server;
		}
		
		String groupId = txTransactionLocal.getGroupId();
		
		String appName = server.getMetaInfo().getAppName();
		
		String key = groupId + "_" + appName;
		
		Server cachedServer = (Server) Constants.cacheModelInfo.get(key);
		if(cachedServer == null){
			logger.info("The server of key:" + key + " has not been cached yet!");
			Constants.cacheModelInfo.put(key, server);
			return server;
		} else{
			logger.info("The cached server info, host:" + cachedServer.getHost() + ", port:" + cachedServer.getPort());
			return cachedServer;
		}
		
	}
	
}
