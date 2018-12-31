package com.codingapi.tx.manager.support;

import com.codingapi.tx.spi.rpc.params.NotifyConnectParams;
import com.codingapi.tx.manager.config.TxManagerConfig;
import com.codingapi.tx.manager.db.ManagerStorage;
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

    @Autowired
    private ManagerStorage managerStorage;

    @Autowired
    private RestTemplate restTemplate;

    private String managerRefreshUrl = "http://%s/manager/refresh";


    @Autowired
    private TxManagerConfig txManagerConfig;


    public void refresh() {
        NotifyConnectParams notifyConnectParams = new NotifyConnectParams();
        notifyConnectParams.setHost(txManagerConfig.getManagerHost());
        notifyConnectParams.setPort(txManagerConfig.getRpcPort());

        List<String> addressList =  managerStorage.addressList();
        log.info("addressList->{}",addressList);
        for(String address:addressList){
            String url = String.format(managerRefreshUrl,address);
            log.info("url->{}",url);
            try {
                ResponseEntity<Boolean> res =  restTemplate.postForEntity(String.format(managerRefreshUrl,address), notifyConnectParams,Boolean.class);
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
