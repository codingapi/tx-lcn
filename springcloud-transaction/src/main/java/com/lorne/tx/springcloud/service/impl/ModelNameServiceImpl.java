package com.lorne.tx.springcloud.service.impl;

import com.lorne.core.framework.utils.encode.MD5Util;
import com.lorne.tx.service.ModelNameService;
import com.lorne.tx.springcloud.listener.ServerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by lorne on 2017/7/12.
 */
@Service
@Configuration
public class ModelNameServiceImpl implements ModelNameService {

    @Value("${spring.application.name}")
    private String modelName;

    @Autowired
    private ServerListener serverListener;

    @Override
    public String getModelName() {
        return modelName;
    }

    private String getIp(){
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return host;
    }

    @Override
    public String getUniqueKey() {
        String address = getIp()+serverListener.getPort();
        return  MD5Util.string2MD5(address);
    }
}
