package com.codingapi.tx.client.aspectlog;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

/**
 * @author lorne
 * @date 2018/12/20
 * @description
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
