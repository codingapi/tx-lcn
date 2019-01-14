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
package com.codingapi.txlcn.client.aspectlog;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

/**
 * @author lorne
 */
@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(value = "tx-lcn.aspect.log")
public class AspectLogDbProperties {

    public AspectLogDbProperties(@Value("${spring.application.name}") String applicationName, @Value("${server.port}") Integer port) {
        if (Objects.isNull(applicationName)) {
            applicationName = "localhost-" + port;
        }
        this.filePath = System.getProperty("user.dir") + File.separator + ".txlcn" + File.separator + applicationName;
    }

    private String filePath;

}
