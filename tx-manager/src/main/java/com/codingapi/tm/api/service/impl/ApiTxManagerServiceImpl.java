package com.codingapi.tm.api.service.impl;


import com.codingapi.tm.api.service.ApiTxManagerService;
import com.codingapi.tm.compensate.model.TransactionCompensateMsg;
import com.codingapi.tm.compensate.service.CompensateService;
import com.codingapi.tm.manager.service.EurekaService;
import com.codingapi.tm.manager.service.TxManagerSenderService;
import com.codingapi.tm.manager.service.TxManagerService;
import com.codingapi.tm.model.TxServer;
import com.codingapi.tm.model.TxState;
import com.codingapi.tm.redis.service.RedisServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lorne on 2017/7/1.
 */
@Service
public class ApiTxManagerServiceImpl implements ApiTxManagerService {


    @Autowired
    private TxManagerService managerService;

    @Autowired
    private EurekaService eurekaService;

    @Autowired
    private CompensateService compensateService;


    @Autowired
    private TxManagerSenderService txManagerSenderService;


    @Override
    public TxServer getServer() {
        return eurekaService.getServer();
    }


    @Override
    public boolean clearTransaction(String groupId, String taskId, int isGroup) {
        return managerService.clearTransaction(groupId,taskId,isGroup);
    }

    @Override
    public int getTransaction(String groupId, String taskId) {
        return managerService.getTransaction(groupId, taskId);
    }

    @Override
    public boolean sendCompensateMsg(long currentTime, String groupId, String model, String address, String uniqueKey, String className, String methodStr, String data, int time) {
        TransactionCompensateMsg transactionCompensateMsg = new TransactionCompensateMsg(currentTime, groupId, model, address, uniqueKey, className, methodStr, data, time, 0);
        return compensateService.saveCompensateMsg(transactionCompensateMsg);
    }

    @Override
    public String sendMsg(String model,String msg) {
        return txManagerSenderService.sendMsg(model, msg);
    }


    @Override
    public TxState getState() {
        return eurekaService.getState();
    }
}
