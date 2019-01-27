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
public class ApplicationInformation {

    /**
     * 模块标识
     *
     * @param environment      Spring Env
     * @param serverProperties serverProperties
     * @return 标识
     */
    public static String modId(ConfigurableEnvironment environment, ServerProperties serverProperties) {

        String applicationName = environment.getProperty("spring.application.name");
        applicationName = StringUtils.hasText(applicationName) ? applicationName : "application";
        return applicationName + ":" + serverPort(serverProperties);
    }

    /**
     * 拆分网络地址为host and port
     *
     * @param hostAndPort 主机和端口
     * @return 主机端口数组
     */
    public static String[] splitAddress(String hostAndPort) {
        if (hostAndPort.indexOf(':') == -1) {
            throw new IllegalStateException("non exists port");
        }
        String[] result = hostAndPort.split(":");
        if (result[0].charAt(0) == '/') {
            result[0] = result[0].substring(1);
        }
        return result;
    }

    /**
     * 模块HTTP端口号
     *
     * @param serverProperties serverProperties
     * @return int
     */
    public static int serverPort(ServerProperties serverProperties) {
        return Objects.isNull(serverProperties) ? 0 : (Objects.isNull(serverProperties.getPort()) ? 8080 :
                serverProperties.getPort());
    }
}
