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
package com.codingapi.txlcn.logger.db;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author lorne
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "tx-lcn.logger")
@Component
@Slf4j
public class LogDbProperties extends HikariConfig {

    private boolean enabled = false;

    private boolean onlyError = true;

    public LogDbProperties(@Autowired(required = false) DataSourceProperties dataSourceProperties) {
        if (Objects.isNull(dataSourceProperties) ||
                Objects.isNull(dataSourceProperties.getDriverClassName()) ||
                Objects.isNull(dataSourceProperties.getUrl())) {
            log.info("TxLogger used user's config.");
            return;
        }
        this.setDriverClassName(dataSourceProperties.getDriverClassName());
        this.setJdbcUrl(dataSourceProperties.getUrl());
        this.setUsername(dataSourceProperties.getUsername());
        this.setPassword(dataSourceProperties.getPassword());
    }
}
