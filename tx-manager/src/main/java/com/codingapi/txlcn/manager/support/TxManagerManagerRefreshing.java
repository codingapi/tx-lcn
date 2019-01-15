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
package com.codingapi.txlcn.manager.support;

import com.codingapi.txlcn.spi.message.params.NotifyConnectParams;
import com.codingapi.txlcn.manager.config.TxManagerConfig;
import com.codingapi.txlcn.manager.db.ManagerStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@Component
@Slf4j
public class TxManagerManagerRefreshing {

    private final ManagerStorage managerStorage;

    private final RestTemplate restTemplate;

    private final TxManagerConfig txManagerConfig;

    private static final String MANAGER_REFRESH_URL = "http://%s/manager/refresh";

    @Autowired
    public TxManagerManagerRefreshing(ManagerStorage managerStorage,
                                      RestTemplate restTemplate, TxManagerConfig txManagerConfig) {
        this.managerStorage = managerStorage;
        this.restTemplate = restTemplate;
        this.txManagerConfig = txManagerConfig;
    }


    public void refresh() {
        NotifyConnectParams notifyConnectParams = new NotifyConnectParams();
        notifyConnectParams.setHost(txManagerConfig.getHost());
        notifyConnectParams.setPort(txManagerConfig.getPort());

        List<String> addressList =  managerStorage.addressList();
        log.info("addressList->{}",addressList);
        for(String address:addressList){
            String url = String.format(MANAGER_REFRESH_URL,address);
            log.info("url->{}",url);
            try {
                ResponseEntity<Boolean> res =  restTemplate.postForEntity(String.format(MANAGER_REFRESH_URL,address), notifyConnectParams,Boolean.class);
                if(res.getStatusCode().equals(HttpStatus.OK)||res.getStatusCode().is5xxServerError()) {
                    log.info("manager refresh res->{}", res);
                }else{
                    managerStorage.remove(address);
                }
            }catch (Exception e){
                log.error("manager refresh error ",e);

                //check exception then remove.
                if( e instanceof ResourceAccessException){
                    ResourceAccessException resourceAccessException = (ResourceAccessException)e;

                    if(resourceAccessException.getCause()!=null && resourceAccessException.getCause() instanceof ConnectException){
                        //can't access .
                        managerStorage.remove(address);
                    }
                }

            }
        }
    }
}
