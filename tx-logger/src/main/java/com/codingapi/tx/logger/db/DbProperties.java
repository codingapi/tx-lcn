package com.codingapi.tx.logger.db;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lorne
 * @date 2018/12/20
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(value = "tx-lcn.logger")
public class DbProperties extends HikariConfig {
    private boolean enabled = false;
}
