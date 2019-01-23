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
package com.codingapi.txlcn.tc.corelog;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Objects;

/**
 * @author lorne
 */
@Data
@Component
@ConfigurationProperties(value = "tx-lcn.aspect.log")
public class H2DbProperties {

    private String filePath;

    public H2DbProperties(
            @Autowired(required = false) ConfigurableEnvironment environment,
            @Autowired(required = false) ServerProperties serverProperties) {
        String applicationName = "application";
        Integer port = 0;
        if (Objects.nonNull(environment)) {
            applicationName = environment.getProperty("spring.application.name");
        }
        if (Objects.nonNull(serverProperties)) {
            port = serverProperties.getPort();
        }
        this.filePath = System.getProperty("user.dir") +
                File.separator +
                ".txlcn" +
                File.separator +
                (StringUtils.hasText(applicationName) ? applicationName : "application") +
                "-" + port;
    }
}
