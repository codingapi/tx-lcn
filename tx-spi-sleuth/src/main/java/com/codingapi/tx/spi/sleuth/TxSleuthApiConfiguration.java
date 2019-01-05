package com.codingapi.tx.spi.sleuth;

import com.codingapi.tx.spi.sleuth.listener.DefaultSleuthParamListener;
import com.codingapi.tx.spi.sleuth.listener.SleuthParamListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/14
 *
 * @author lorne
 */
@Configuration
@ComponentScan
public class TxSleuthApiConfiguration {


    static {
        String propagationKeys = System.getProperty("spring.sleuth.propagation-keys");
        if (Objects.isNull(propagationKeys)) {
            propagationKeys = "";
        } else if (!propagationKeys.trim().endsWith(",")) {
            propagationKeys += ",";
        }
        System.setProperty("spring.sleuth.propagation-keys",
                propagationKeys + TracerHelper.GROUP_ID_FIELD_NAME + ","
                        + TracerHelper.TX_APP_LIST + "," + TracerHelper.TX_MANAGER_FIELD_NAME);
    }

    @Bean
    @ConditionalOnMissingBean
    public SleuthParamListener sleuthParamListener() {
        return new DefaultSleuthParamListener();
    }
}