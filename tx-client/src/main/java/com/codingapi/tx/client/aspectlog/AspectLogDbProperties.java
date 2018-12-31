package com.codingapi.tx.client.aspectlog;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lorne
 * @date 2018/12/20
 * @description
 */
@Data
@ConfigurationProperties(value = "tx-lcn.aspect.log")
public class AspectLogDbProperties {

    private String filePath;

}
