/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.common.util;

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
        int index = hostAndPort.lastIndexOf(":");
        String host = hostAndPort.substring(0, index);
        String port = hostAndPort.substring(index + 1);
        String[] result = {host, port};
        if (StringUtils.isEmpty(result[0])) {
            result[0] = "0.0.0.0";
            return result;
        }
        if (result[0].charAt(0) == '/') {
            result[0] = result[0].substring(1);
            return result;
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
