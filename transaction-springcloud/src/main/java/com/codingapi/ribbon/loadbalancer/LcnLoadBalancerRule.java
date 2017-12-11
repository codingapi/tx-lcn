package com.codingapi.ribbon.loadbalancer;

import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.lorne.core.framework.utils.encode.MD5Util;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * created by foxdd 2017-12-05
 */
public class LcnLoadBalancerRule {
	
	private Logger logger = LoggerFactory.getLogger(LcnLoadBalancerRule.class);
	
	public Server proxy(List<Server> qualifiedServers, Server server){
		logger.info("The selected server info, host:" + server.getHost() + ", port:" + server.getPort());
		TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
		if(txTransactionLocal == null){
			return server;
		}
		
		String groupId = txTransactionLocal.getGroupId();

		//取出组件的appName
		String appName = server.getMetaInfo().getAppName();

		logger.info("The model named " + appName + " is going to be called, groupId:" + groupId);

		String key = MD5Util.md5((groupId + "_" + appName).getBytes());
		
		Server cachedServer = getInvoker(txTransactionLocal, qualifiedServers, key);

		if(cachedServer == null){
			logger.info("The server of key:" + key + " has not been cached yet!");
			putInvoker(key, txTransactionLocal, server);
			return server;
		} else{
			logger.info("The cached server info, host:" + cachedServer.getHost() + ", port:" + cachedServer.getPort());
			return cachedServer;
		}
		
	}


	private void putInvoker(String key,TxTransactionLocal txTransactionLocal,Server server){
		String appName = server.getMetaInfo().getAppName();

		String address = server.getHost() + ":" + server.getPort();

		String md5 = MD5Util.md5((address+appName).getBytes());

		logger.info("putServer->address->"+address+",md5-->"+md5);

		txTransactionLocal.putLoadBalance(key,md5);
	}


	private Server getInvoker(TxTransactionLocal txTransactionLocal, List<Server> servers,String key){
		String val = txTransactionLocal.getLoadBalance(key);
		if(StringUtils.isEmpty(val)){
			return null;
		}
		for(Server server:servers){
			String appName =  server.getMetaInfo().getAppName();
			//格式统一为host:port
			String address = server.getHost() + ":" + server.getPort();

			String md5 = MD5Util.md5((address+appName).getBytes());

			logger.info("getServer->address->"+address+",md5-->"+md5);

			if(val.equals(md5)){
				return server;
			}
		}
		return null;
	}
	
}
