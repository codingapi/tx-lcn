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
	
	public Server proxy(List<Server> servers,Server server){

		TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
		if(txTransactionLocal==null){
			return server;
		}

		try{
			logger.debug("LCNBalanceProxy - > start");

			String groupId = txTransactionLocal.getGroupId();

			//取出组件的appName
			String appName = server.getMetaInfo().getAppName();


			String key = MD5Util.md5((groupId + "_" + appName).getBytes());

			//如果只有一个可调用模块，则用当前的，且需要将数据记录到redis中
			if(servers.size() == 1){
				putServer(key, txTransactionLocal, server);
				logger.debug("LCNBalanceProxy -> only one server available");
				return server;
			}

			Server oldServer =getServer(txTransactionLocal,servers,key);
			if(oldServer != null){
				logger.debug("LCNBalanceProxy - > load old server ");
				return server;
			}

			putServer(key, txTransactionLocal, server);
			logger.debug("LCNBalanceProxy - > load new server ");

			return server;
		}finally {
			logger.debug("LCNBalanceProxy - > end");
		}
	}



	private void putServer(String key,TxTransactionLocal txTransactionLocal,Server server){
		String serviceName =  server.getMetaInfo().getAppName();
		String address = server.getHostPort();

		String md5 = MD5Util.md5((address+serviceName).getBytes());

		logger.debug("putServer->address->"+address+",md5-->"+md5);

		txTransactionLocal.putLoadBalance(key,md5);
	}


	private Server getServer(TxTransactionLocal txTransactionLocal, List<Server> servers, String key){
		String val = txTransactionLocal.getLoadBalance(key);
		if(StringUtils.isEmpty(val)){
			return null;
		}
		for(Server server:servers){
			String serviceName =  server.getMetaInfo().getAppName();
			String address = server.getHostPort();

			String md5 = MD5Util.md5((address+serviceName).getBytes());

			logger.debug("getServer->address->"+address+",md5-->"+md5);

			if(val.equals(md5)){
				return server;
			}
		}
		return null;
	}
	
}
