package com.codingapi.tx.client.logdb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lorne
 * @date 2018/12/20
 * @description
 */
@Data
@ConfigurationProperties(value = "tx-lcn.log.db")
public class LogDbProperties {

    private String filePath;

}
