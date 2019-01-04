package com.codingapi.tx.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/11/29
 *
 * @author lorne
 */
@Data
@ConfigurationProperties(prefix = "tx-lcn.client")
@Component
public class TxClientConfig {

    public TxClientConfig() {
        this.managerAddress = Collections.singletonList("127.0.0.1:8070");
    }

    /**
     * support order
     *
     * @TxTransaction aop Service
     */
    private int controlOrder = 0;

    /**
     * control order
     *
     * @getConnection() aop service
     */
    private int resourceOrder = 0;

    /**
     * txManager check heart time (s)
     */
    private int txManagerHeart;
    /**
     * txManager max delay time (s)
     */
    private int txManagerDelay;

    /**
     * manager hosts
     */
    private List<String> managerAddress;

    /**
     * Distributed Transaction Time
     */
    private long dtxTime = 30000;

}
