package com.codingapi.tm.compensate.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.tm.compensate.model.TransactionCompensateMsg;
import com.codingapi.tm.compensate.model.TxModel;
import com.codingapi.tm.compensate.service.CompensateService;
import com.codingapi.tm.compensate.dao.CompensateDao;
import com.codingapi.tm.netty.model.TxGroup;
import com.codingapi.tm.redis.service.RedisServerService;
import com.lorne.core.framework.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    @Override
    public List<String> loadModelList() {
        return compensateDao.loadModelList();
    }


    @Override
    public List<String> childModel(String model) {
        return compensateDao.childModel(model);
    }


    @Override
    public List<String> logFile(String path) {
        return compensateDao.logFile(path);
    }

    @Override
    public List<TxModel> logs(String path) {
        List<String> logs = compensateDao.getLogs(path);
        if (logs == null) {
            return null;
        }

        System.out.println(logs);
        List<TxModel> models = new ArrayList<>();
        for (String json : logs) {
            JSONObject jsonObject = JSON.parseObject(json);
            TxModel model = new TxModel();
            long currentTime = jsonObject.getLong("currentTime");
            model.setTime(DateUtil.formatDate(new Date(currentTime), DateUtil.FULL_DATE_TIME_FORMAT));
            models.add(model);
        }
        return models;
    }
}
