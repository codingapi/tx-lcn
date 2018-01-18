package com.codingapi.tx.dubbo.service.impl;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.lorne.core.framework.utils.encode.MD5Util;
import com.codingapi.tx.listener.service.ModelNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by lorne on 2017/7/12.
 */
@Service
public class ModelNameServiceImpl implements ModelNameService {


    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private ProviderConfig providerConfig;

    @Autowired
    private RegistryConfig registryConfig;

    private String host = null;

    @Override
    public String getModelName() {
        return applicationConfig.getName();
    }


    private String getIp() {
        if (host == null) {
            try {
                host = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return host;
    }

    @Override
    public String getUniqueKey() {
        String address = getIp() + providerConfig.getPort();
        return MD5Util.md5(address.getBytes());
    }


    @Override
    public String getIpAddress() {
        return getIp() + ":" + getPort();
    }

    private int getPort(){
        if(registryConfig.getPort()!=null){
            return registryConfig.getPort();
        }
        if(providerConfig.getPort()!=null){
            return providerConfig.getPort();
        }
        return 20880;
    }
}
