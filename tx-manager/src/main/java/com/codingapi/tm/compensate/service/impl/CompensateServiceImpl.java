package com.codingapi.tm.compensate.service.impl;

import com.codingapi.tm.compensate.model.TransactionCompensateMsg;
import com.codingapi.tm.compensate.service.CompensateService;
import com.codingapi.tm.compensate.dao.CompensateDao;
import com.codingapi.tm.netty.model.TxGroup;
import com.codingapi.tm.redis.service.RedisServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class CompensateServiceImpl implements CompensateService {


    @Autowired
    private CompensateDao compensateDao;


    @Autowired
    private RedisServerService redisServerService;

    @Override
    public boolean saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg) {

        TxGroup txGroup = redisServerService.getTxGroupById(transactionCompensateMsg.getGroupId());
        if (txGroup == null) {
            txGroup = redisServerService.getTxGroupOnNotifyById(transactionCompensateMsg.getGroupId());
            if (txGroup != null) {
                redisServerService.deleteNotifyTxGroup(transactionCompensateMsg.getGroupId());
            }
        }
        redisServerService.deleteTxGroup(transactionCompensateMsg.getGroupId());

        transactionCompensateMsg.setTxGroup(txGroup);

        return compensateDao.saveCompensateMsg(transactionCompensateMsg);
    }
}
