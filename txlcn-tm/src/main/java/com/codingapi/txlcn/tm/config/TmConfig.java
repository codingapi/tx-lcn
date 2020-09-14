package com.codingapi.txlcn.tm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020/9/7 15:00
 */
@Configuration
@ConfigurationProperties(prefix = "txlcn.protocol")
@Data
public class TmConfig {
    private String name;
    private int port;
}
