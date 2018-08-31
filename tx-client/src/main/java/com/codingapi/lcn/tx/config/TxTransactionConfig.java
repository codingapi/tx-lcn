package com.codingapi.lcn.tx.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lorne
 * @date 2018/8/31
 * @description
 */

@NoArgsConstructor
@Data
@ConfigurationProperties(prefix = "tx.client")
@Component
public class TxTransactionConfig {

    /**
     * 事务处理切面顺序.
     * default 0
     */
    private int transactionAspectOrder = 0;


    /**
     * jdbc切面顺序
     * default 0
     */
    private int jdbcAspectOrder=0;


}
