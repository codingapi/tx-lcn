package com.codingapi.tm.redis;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by lorne on 2017/10/31.
 */

@Component
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisProperties {


    private String nodes;

    private Integer commandTimeout;

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public Integer getCommandTimeout() {
        return commandTimeout;
    }

    public void setCommandTimeout(Integer commandTimeout) {
        this.commandTimeout = commandTimeout;
    }
}
