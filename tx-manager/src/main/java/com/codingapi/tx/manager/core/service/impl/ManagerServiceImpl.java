package com.codingapi.tx.manager.core.service.impl;

import com.codingapi.tx.client.springcloud.spi.message.params.NotifyConnectParams;
import com.codingapi.tx.manager.core.service.ManagerService;
import com.codingapi.tx.manager.support.message.MessageCreator;
import com.codingapi.tx.client.springcloud.spi.message.RpcClient;
import com.codingapi.tx.client.springcloud.spi.message.exception.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@Service
public class ManagerServiceImpl implements ManagerService {


    @Autowired
    private RpcClient rpcClient;


    @Override
    public boolean refresh( NotifyConnectParams notifyConnectParams) throws RpcException {
        List<String> keys =  rpcClient.loadAllRemoteKey();
        if(keys!=null&&keys.size()>0){
            for(String key:keys){
                rpcClient.send(key, MessageCreator.newTxManager(notifyConnectParams));
            }
        }
        return true;
    }
}
