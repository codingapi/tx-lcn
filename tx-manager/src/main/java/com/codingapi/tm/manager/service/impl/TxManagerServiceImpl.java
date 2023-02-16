package com.codingapi.tm.manager.service.impl;


import com.codingapi.tm.Constants;
import com.codingapi.tm.compensate.service.CompensateService;
import com.codingapi.tm.manager.ModelInfoManager;
import com.codingapi.tm.manager.service.LoadBalanceService;
import com.codingapi.tm.manager.service.TxManagerSenderService;
import com.codingapi.tm.manager.service.TxManagerService;
import com.codingapi.tm.config.ConfigReader;
import com.codingapi.tm.model.ModelInfo;
import com.codingapi.tm.netty.model.TxGroup;
import com.codingapi.tm.netty.model.TxInfo;
import com.codingapi.tm.redis.service.RedisServerService;
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


    @Autowired
    private LoadBalanceService loadBalanceService;

    @Autowired
    private CompensateService compensateService;

    private Logger logger = LoggerFactory.getLogger(TxManagerServiceImpl.class);


    @Override
    public TxGroup createTransactionGroup(String groupId) {
        logger.info("创建事物组");
        //新建事务对象
        TxGroup txGroup = new TxGroup();
        //从redis缓存中获取是否有补偿信息
        if (compensateService.getCompensateByGroupId(groupId)!=null) {
            txGroup.setIsCompensate(1);
        }

        txGroup.setStartTime(System.currentTimeMillis());
        txGroup.setGroupId(groupId);
        //将事务信息存储redis中
        String key = configReader.getKeyPrefix() + groupId;
        redisServerService.saveTransaction(key, txGroup.toJsonString());
        return txGroup;
    }


    @Override
    public TxGroup addTransactionGroup(String groupId, String taskId, int isGroup, String channelAddress, String methodStr) {
        logger.info("添加事务组子对象...");
        //构建key，并从redis获取事务信息
        String key = getTxGroupKey(groupId);
        TxGroup txGroup = getTxGroup(groupId);
        //说明事务在redis中不存在，则直接返回。
        if (txGroup==null) {
            return null;
        }
        //封装参与方事务信息
        TxInfo txInfo = new TxInfo();
        txInfo.setChannelAddress(channelAddress);
        txInfo.setKid(taskId);//参与方内部的任务，主要用来接受当所有参与方执行后，发起者告知txm是否通知大伙儿一起提交还是回滚。
        txInfo.setAddress(Constants.address);
        txInfo.setIsGroup(isGroup);
        txInfo.setMethodStr(methodStr);
        //通过发起方地址，从缓存中寻找到注册信息。
        ModelInfo modelInfo =  ModelInfoManager.getInstance().getModelByChannelName(channelAddress);
        if(modelInfo!=null) {//将发起方地址信息，存储事务信息中
            txInfo.setUniqueKey(modelInfo.getUniqueKey());
            txInfo.setModelIpAddress(modelInfo.getIpAddress());
            txInfo.setModel(modelInfo.getModel());
        }
        //
        txGroup.addTransactionInfo(txInfo);
        //添加redis缓存中
        redisServerService.saveTransaction(key, txGroup.toJsonString());

        return txGroup;
    }

    @Override
    public boolean rollbackTransactionGroup(String groupId) {
        logger.info("设置强制回滚事务...");
        String key = getTxGroupKey(groupId);
        TxGroup txGroup = getTxGroup(groupId);
        if (txGroup==null) {
            return false;
        }
        txGroup.setRollback(1);
        redisServerService.saveTransaction(key, txGroup.toJsonString());
        return true;
    }

    @Override
    public int cleanNotifyTransaction(String groupId, String taskId) {
        logger.info("检查事务组数据...");
        int res = 0;
        logger.info("start-cleanNotifyTransaction->groupId:"+groupId+",taskId:"+taskId);
        String key = getTxGroupKey(groupId);
        TxGroup txGroup = getTxGroup(groupId);
        if (txGroup==null) {
            logger.info("cleanNotifyTransaction - > txGroup is null ");
            return res;
        }

        if(txGroup.getHasOver()==0){

            //整个事务回滚.
            txGroup.setRollback(1);
            redisServerService.saveTransaction(key, txGroup.toJsonString());

            logger.info("cleanNotifyTransaction - > groupId "+groupId+" not over,all transaction must rollback !");
            return 0;
        }

        if(txGroup.getRollback()==1){
            logger.info("cleanNotifyTransaction - > groupId "+groupId+" only rollback !");
            return 0;
        }

        //更新数据
        boolean hasSet = false;
        for (TxInfo info : txGroup.getList()) {
            if (info.getKid().equals(taskId)) {
                if(info.getNotify()==0&&info.getIsGroup()==0) {
                    info.setNotify(1);
                    hasSet = true;
                    res = 1;

                    break;
                }
            }
        }

        //判断是否都结束
        boolean isOver = true;
        for (TxInfo info : txGroup.getList()) {
            if (info.getIsGroup() == 0 && info.getNotify() == 0) {
                isOver = false;
                break;
            }
        }

        if (isOver) {
            deleteTxGroup(txGroup);
        }

        //有更新的数据，需要修改记录
        if(!isOver&&hasSet) {
            redisServerService.saveTransaction(key, txGroup.toJsonString());
        }

        logger.info("end-cleanNotifyTransaction->groupId:"+groupId+",taskId:"+taskId+",res(1:commit,0:rollback):"+res);
        return res;
    }


    @Override
    public int closeTransactionGroup(String groupId,int state) {
        logger.info("关闭事务组");
        //构建key，从redis获取事务信息
        String key = getTxGroupKey(groupId);
        TxGroup txGroup = getTxGroup(groupId);
        //为空则说明事务不存在，返回失败。
        if(txGroup==null){
            return 0;
        }
        //设置发起方事务执行结果
        txGroup.setState(state);
        //标记事务结束
        txGroup.setHasOver(1);
        //存储redis中
        redisServerService.saveTransaction(key,txGroup.toJsonString());
        return transactionConfirmService.confirm(txGroup);
    }


    @Override
    public void dealTxGroup(TxGroup txGroup, boolean hasOk) {
        if(hasOk) {
            deleteTxGroup(txGroup);
        }
    }


    @Override
    public void deleteTxGroup(TxGroup txGroup) {
        String groupId = txGroup.getGroupId();

        String key = getTxGroupKey(groupId);
        redisServerService.deleteKey(key);

        loadBalanceService.remove(groupId);
    }


    @Override
    public TxGroup getTxGroup(String groupId) {
        String key = getTxGroupKey(groupId);
        return redisServerService.getTxGroupByKey(key);
    }

    @Override
    public String getTxGroupKey(String groupId) {
        return configReader.getKeyPrefix() + groupId;
    }
}
