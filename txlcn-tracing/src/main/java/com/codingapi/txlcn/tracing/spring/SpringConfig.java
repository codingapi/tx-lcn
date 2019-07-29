package com.codingapi.txlcn.tracing.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author dinghuang123@gmail.com
 * @since 2019/7/29
 */
@Component
public class SpringConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfig.class);

    @Value("${server.port}")
    public Integer port;

    @Value("${spring.application.name}")
    public String serviceName;

    public String host;

    public Integer getPort() {
        return this.port;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public String getHost() {
        if (this.host == null) {
            InetAddress address = null;
            try {
                address = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                LOGGER.info(e.getMessage());
            }
            this.host = address.getHostAddress();
        }
        return this.host;
    }

}
