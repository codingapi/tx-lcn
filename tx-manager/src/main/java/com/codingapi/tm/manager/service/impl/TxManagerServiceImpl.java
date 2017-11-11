package com.codingapi.tm.manager.service.impl;


import com.codingapi.tm.Constants;
import com.codingapi.tm.manager.service.TxManagerSenderService;
import com.codingapi.tm.manager.service.TxManagerService;
import com.codingapi.tm.config.ConfigReader;
import com.codingapi.tm.netty.model.TxGroup;
import com.codingapi.tm.netty.model.TxInfo;
import com.codingapi.tm.redis.service.RedisServerService;
import com.lorne.core.framework.utils.KidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lorne on 2017/6/7.
 */
@Service
public class TxManagerServiceImpl implements TxManagerService {



    @Autowired
    private ConfigReader configReader;

    @Autowired
    private RedisServerService redisServerService;


    @Autowired
    private TxManagerSenderService transactionConfirmService;



    private Logger logger = LoggerFactory.getLogger(TxManagerServiceImpl.class);


    @Override
    public TxGroup createTransactionGroup() {
        String groupId = KidUtils.generateShortUuid();
        TxGroup txGroup = new TxGroup();
        txGroup.setStartTime(System.currentTimeMillis());
        txGroup.setGroupId(groupId);

        redisServerService.createTransactionGroup(groupId,txGroup.toJsonString());

        return txGroup;
    }

    @Override
    public TxGroup addTransactionGroup(String groupId,String uniqueKey, String taskId,int isGroup, String modelName) {

        TxGroup txGroup = redisServerService.getTxGroupById(groupId);
        if (txGroup==null) {
            return null;
        }
        TxInfo txInfo = new TxInfo();
        txInfo.setModelName(modelName);
        txInfo.setKid(taskId);
        txInfo.setAddress(Constants.address);
        txInfo.setIsGroup(isGroup);
        txInfo.setUniqueKey(uniqueKey);
        txGroup.addTransactionInfo(txInfo);

        redisServerService.updateTransactionGroup(groupId,txGroup.toJsonString());
        return txGroup;
    }

    @Override
    public  int getTransaction(String groupId, String taskId) {
        boolean inNotify = false;
        TxGroup txGroup = redisServerService.getTxGroupById(groupId);
        if (txGroup==null) {
            txGroup = redisServerService.getTxGroupOnNotifyById(groupId);
            inNotify = true;
            if(txGroup==null){
                return 0;
            }
        }

        if(txGroup.getHasOver()==0){
            long nowTime = System.currentTimeMillis();
            long startTime =  txGroup.getStartTime();
            //超时清理数据
            if(nowTime-startTime>(configReader.getRedisSaveMaxTime()*1000)){
                if(inNotify){
                    redisServerService.deleteTxGroup(groupId);
                }else{
                    redisServerService.deleteNotifyTxGroup(groupId);
                }
                return 0;
            }
            return -1;
        }
        boolean res = txGroup.getState() == 1;
        if(!res) {
            return 0;
        }

        for (TxInfo info : txGroup.getList()) {
            if (info.getKid().equals(taskId)) {
                return info.getNotify()==0?1:0;
            }
        }

        return 0;
    }


    @Override
    public boolean clearTransaction(String groupId, String taskId, int isGroup) {
        logger.info("start-clearTransaction->groupId:"+groupId+",taskId:"+taskId);
        boolean isNotify = false;
        TxGroup txGroup = redisServerService.getTxGroupById(groupId);
        if (txGroup==null) {
            txGroup = redisServerService.getTxGroupOnNotifyById(groupId);
            isNotify=true;
            if (txGroup==null) {
                return false;
            }
        }
        boolean res = txGroup.getState() == 1;

        boolean hasSet = false;
        for (TxInfo info : txGroup.getList()) {
            if (info.getKid().equals(taskId)) {
                info.setNotify(1);
                hasSet = true;
            }
        }

        if(hasSet&&res) {
            redisServerService.updateNotifyTransactionGroup(groupId,txGroup.toJsonString());
        }

        boolean isOver = true;
        for (TxInfo info : txGroup.getList()) {
            if(isGroup==1) {
                if (info.getIsGroup() == 0 && info.getNotify() == 0) {
                    isOver = false;
                    break;
                }
            }else{
                if (info.getNotify() == 0) {
                    isOver = false;
                    break;
                }
            }
        }

        if (isOver&&!isNotify) {
            redisServerService.deleteTxGroup(groupId);
        }

        logger.info("end-clearTransaction->groupId:"+groupId+",taskId:"+taskId+",res:"+res);
        return res;
    }


    @Override
    public boolean closeTransactionGroup(String groupId,int state) {
        TxGroup txGroup = redisServerService.getTxGroupById(groupId);
        if(txGroup==null){
            return false;
        }
        txGroup.setState(state);
        txGroup.setHasOver(1);
        return transactionConfirmService.confirm(txGroup);
    }


    @Override
    public void dealTxGroup(TxGroup txGroup, boolean hasOk) {
        if (!hasOk) {
            //未通知成功
            if (txGroup.getState() == 1) {
                redisServerService.updateNotifyTransactionGroup(txGroup.getGroupId(),txGroup.toJsonString());
            }
        }
        redisServerService.deleteTxGroup(txGroup.getGroupId());
    }


    @Override
    public void deleteTxGroup(TxGroup txGroup) {
        redisServerService.deleteTxGroup(txGroup.getGroupId());
    }



}
