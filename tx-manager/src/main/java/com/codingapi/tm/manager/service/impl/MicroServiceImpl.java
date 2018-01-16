package com.codingapi.tm.manager.service.impl;

import com.codingapi.tm.Constants;
import com.codingapi.tm.config.ConfigReader;
import com.codingapi.tm.framework.utils.IpAddressUtils;
import com.codingapi.tm.framework.utils.SocketManager;
import com.codingapi.tm.manager.service.MicroService;
import com.codingapi.tm.model.TxServer;
import com.codingapi.tm.model.TxState;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class MicroServiceImpl implements MicroService {


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ConfigReader configReader;


    @Autowired
    private DiscoveryClient discoveryClient;


    @Autowired
    private EurekaClient eurekaClient;


    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(MicroServiceImpl.class);



    public List<InstanceInfo> getConfigServiceInstances() {
        Application application = eurekaClient.getApplication(tmKey);
        if (application == null) {
            logger.error("get eureka server error!");
        }
        return application != null ? application.getInstances() : new ArrayList<>();
    }

    @Override
    public TxState getState() {
        TxState state = new TxState();

        //String ipAddress = EurekaServerContextHolder.getInstance().getServerContext().getApplicationInfoManager().getEurekaInstanceConfig().getIpAddress();
        String ipAddress = getIp(discoveryClient.getLocalServiceInstance().getHost());
        state.setIp(ipAddress);
        state.setPort(Constants.socketPort);
        state.setMaxConnection(SocketManager.getInstance().getMaxConnection());
        state.setNowConnection(SocketManager.getInstance().getNowConnection());
        state.setRedisSaveMaxTime(configReader.getRedisSaveMaxTime());
        state.setTransactionNettyDelayTime(configReader.getTransactionNettyDelayTime());
        state.setTransactionNettyHeartTime(configReader.getTransactionNettyHeartTime());
        state.setNotifyUrl(configReader.getCompensateNotifyUrl());
        state.setCompensate(configReader.isCompensateAuto());
        state.setCompensateTryTime(configReader.getCompensateTryTime());
        state.setAutoCompensateLimit(configReader.getAutoCompensateLimit());
        state.setSlbList(getServices());
        return state;
    }


    private String getIp(String ipAddress){
        if(!IpAddressUtils.isIp(ipAddress)){

            ipAddress = IpAddressUtils.getIpByDomain(ipAddress);

            if(ipAddress==null||!IpAddressUtils.isIp(ipAddress)) {
                ipAddress = "127.0.0.1";
            }
        }
        return ipAddress;
    }

    private List<String> getServices(){
        List<String> urls = new ArrayList<>();
        List<InstanceInfo> instanceInfos =getConfigServiceInstances();
        for (InstanceInfo instanceInfo : instanceInfos) {
            String url = instanceInfo.getHomePageUrl();
            String address = instanceInfo.getIPAddr();
            String ipAddress = getIp(address);

            url = url.replace(address,ipAddress);
            urls.add(url);
        }
        return urls;
    }

    @Override
    public TxServer getServer() {
        List<String> urls= getServices();
        List<TxState> states = new ArrayList<>();
        for(String url:urls){
            try {
                TxState state = restTemplate.getForObject(url + "/tx/manager/state", TxState.class);
                states.add(state);
            } catch (Exception e) {
            }

        }
        if(states.size()<=1) {
            TxState state = getState();
            if (state.getMaxConnection() > state.getNowConnection()) {
                return TxServer.format(state);
            } else {
                return null;
            }
        }else{
            //找默认数据
            TxState state = getDefault(states,0);
            if (state == null) {
                //没有满足的默认数据
                return null;
            }
            return TxServer.format(state);
        }
    }

    private TxState getDefault(List<TxState> states, int index) {
        TxState state = states.get(index);
        if (state.getMaxConnection() == state.getNowConnection()) {
            index++;
            if (states.size() - 1 >= index) {
                return getDefault(states, index);
            } else {
                return null;
            }
        } else {
            return state;
        }
    }

}
