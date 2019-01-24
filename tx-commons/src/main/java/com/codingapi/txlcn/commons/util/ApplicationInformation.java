package com.codingapi.txlcn.commons.util;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Description:
 * Date: 19-1-24 下午1:44
 *
 * @author ujued
 */
public abstract class ApplicationInformation {

    /**
     * 模块标识
     *
     * @param environment      Spring Env
     * @param serverProperties serverProperties
     * @return 标识
     */
    public static String modId(ConfigurableEnvironment environment, ServerProperties serverProperties) {
        int port = Objects.isNull(serverProperties) ? 0 : (Objects.isNull(serverProperties.getPort()) ? 8080 :
                serverProperties.getPort());
        String applicationName = environment.getProperty("spring.application.name");
        applicationName = StringUtils.hasText(applicationName) ? applicationName : "application";
        return applicationName + ":" + port;
    }
}
