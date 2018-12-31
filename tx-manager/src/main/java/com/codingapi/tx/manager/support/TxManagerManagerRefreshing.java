package com.codingapi.tx.manager.support;

import com.codingapi.tx.spi.rpc.params.NotifyConnectParams;
import com.codingapi.tx.manager.config.TxManagerConfig;
import com.codingapi.tx.manager.db.ManagerStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
                boolean res =  restTemplate.postForObject(String.format(managerRefreshUrl,address), notifyConnectParams,Boolean.class);
                log.info("manager refresh res->{}",res);
            }catch (Exception e){
                managerStorage.remove(address);
                log.error("manager refresh error ",e);
            }
        }
    }
}
