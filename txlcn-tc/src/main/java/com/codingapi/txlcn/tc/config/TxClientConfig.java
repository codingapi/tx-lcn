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
package com.codingapi.txlcn.tc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
        this.dtxAspectOrder = 0;
        this.dtxTime = 30 * 1000;
        this.managerAddress = Collections.singletonList("127.0.0.1:8070");
    }

    /**
     * aspect order
     */
    private Integer dtxAspectOrder;

    /**
     * aspect connection order
     */
    private int resourceOrder;

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
     * 调用链长度等级
     */
    private int chainLevel = 3;

    /**
     * Distributed Transaction Time
     */
    private long dtxTime;

    private long tmRpcTimeout;

    private long machineId;

    private void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    private void setDtxTime(long dtxTime) {
        this.dtxTime = dtxTime;
    }

    private void setTmRpcTimeout(long timeout) {
        this.tmRpcTimeout = timeout;
    }

    public void applyTmRpcTimeout(long timeout) {
        setTmRpcTimeout(timeout);
    }

    public void applyDtxTime(long dtxTime) {
        setDtxTime(dtxTime);
    }

    public void applyMachineId(long machineId) {
        setMachineId(machineId);
    }
}
