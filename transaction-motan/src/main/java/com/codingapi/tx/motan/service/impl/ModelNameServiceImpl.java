package com.codingapi.tx.motan.service.impl;

import com.codingapi.tx.listener.service.ModelNameService;
import com.lorne.core.framework.utils.encode.MD5Util;
import com.weibo.api.motan.config.springsupport.BasicServiceConfigBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * 2017/11/17 13:30
 */
@Service
public class ModelNameServiceImpl implements ModelNameService {

    @Resource
    private BasicServiceConfigBean basicServiceConfigBean;

    @Resource
    private Environment environment;

    private String host = null;

    public String getModelName() {
        return environment.getProperty("tx.application");
    }

    public String getUniqueKey() {
        String address = getIp() + getPort();
        return MD5Util.md5(address.getBytes());
    }

    public String getIpAddress() {
        String address = getIp() + ":" + getPort();
        return address;
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

    private String getPort() {
        String export = basicServiceConfigBean.getExport();
        if (StringUtils.isNotBlank(export)) {
            return export.split(":")[1];
        }
        return null;
    }

    /**
     * 超时时间
     *
     * @return  超时时间
     */
    public String getTimeOut() {
        return basicServiceConfigBean.getRequestTimeout().toString();
    }
}
