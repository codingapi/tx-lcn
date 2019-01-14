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
package com.codingapi.txlcn.spi.sleuth;

import com.codingapi.txlcn.spi.sleuth.listener.DefaultSleuthParamListener;
import com.codingapi.txlcn.spi.sleuth.listener.SleuthParamListener;
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