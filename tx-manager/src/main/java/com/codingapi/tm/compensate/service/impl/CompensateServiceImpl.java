package com.codingapi.tm.compensate.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.tm.compensate.dao.CompensateDao;
import com.codingapi.tm.compensate.model.TransactionCompensateMsg;
import com.codingapi.tm.compensate.model.TxModel;
import com.codingapi.tm.compensate.service.CompensateService;
import com.codingapi.tm.config.ConfigReader;
import com.codingapi.tm.manager.ModelInfoManager;
import com.codingapi.tm.manager.service.TxManagerSenderService;
import com.codingapi.tm.model.ModelInfo;
import com.codingapi.tm.netty.model.TxGroup;
import com.codingapi.tm.netty.model.TxInfo;
import com.codingapi.tm.redis.service.RedisServerService;
import com.lorne.core.framework.exception.ServiceException;
import com.lorne.core.framework.utils.DateUtil;
import com.lorne.core.framework.utils.encode.Base64Utils;
import com.lorne.core.framework.utils.http.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class CompensateServiceImpl implements CompensateService {


    private Logger logger = LoggerFactory.getLogger(CompensateServiceImpl.class);

    @Autowired
    private CompensateDao compensateDao;

    @Autowired
    private RedisServerService redisServerService;

    @Autowired
    private ConfigReader configReader;

    @Autowired
    private TxManagerSenderService managerSenderService;

    private Executor threadPool = Executors.newFixedThreadPool(20);

    @Override
    public boolean saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg) {


        String key = configReader.getKeyPrefix() + transactionCompensateMsg.getGroupId();
        TxGroup txGroup = redisServerService.getTxGroupByKey(key);
        if (txGroup == null) {
            key = configReader.getKeyPrefixNotify() + transactionCompensateMsg.getGroupId();
            txGroup = redisServerService.getTxGroupByKey(key);
        }
        redisServerService.deleteKey(key);

        transactionCompensateMsg.setTxGroup(txGroup);

        final String json = JSON.toJSONString(transactionCompensateMsg);

        logger.info("补偿->" + json);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = configReader.getCompensateNotifyUrl();
                    logger.error("补偿回调地址->" + url);
                    String res = HttpUtils.postJson(url, json);
                    logger.error("补偿回调结果->" + res);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("补偿回调失败->" + e.getMessage());
                }
            }
        }).start();

        final String compensateKey = compensateDao.saveCompensateMsg(transactionCompensateMsg);

        //自动补偿
        autoCompensate(compensateKey, transactionCompensateMsg);

        return StringUtils.isNotEmpty(compensateKey);

    }


    public void autoCompensate(final String compensateKey, TransactionCompensateMsg transactionCompensateMsg) {
        final String json = JSON.toJSONString(transactionCompensateMsg);
        if (configReader.isCompensateAuto()) {

            logger.info("进入补偿->" + json);
            //自动补偿指导补偿成功...
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final int tryTime = configReader.getCompensateTryTime();
                        boolean isOk = _executeCompensate(json);
                        logger.info("自动补偿结果->" + isOk + ",json->" + json);
                        while (!isOk) {
                            try {
                                Thread.sleep(tryTime * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            isOk = _executeCompensate(json);
                            logger.info("try补偿(补偿失败,进入补偿队列)->" + isOk + ",json->" + json);
                        }

                        compensateDao.deleteCompensateByKey(compensateKey);

                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    @Override
    public List<String> loadModelList() {
        return compensateDao.loadModelList();
    }

    @Override
    public List<String> loadCompensateTimes(String model) {
        return compensateDao.loadCompensateTimes(model);
    }

    @Override
    public List<TxModel> loadCompensateByModelAndTime(String path) {
        List<String> logs = compensateDao.loadCompensateByModelAndTime(path);

        List<TxModel> models = new ArrayList<>();
        for (String json : logs) {
            JSONObject jsonObject = JSON.parseObject(json);
            TxModel model = new TxModel();
            long currentTime = jsonObject.getLong("currentTime");
            model.setTime(DateUtil.formatDate(new Date(currentTime), DateUtil.FULL_DATE_TIME_FORMAT));
            model.setClassName(jsonObject.getString("className"));
            model.setMethod(jsonObject.getString("methodStr"));
            model.setExecuteTime(jsonObject.getInteger("time"));
            model.setBase64(Base64Utils.encode(json.getBytes()));
            model.setState(jsonObject.getInteger("state"));
            model.setOrder(currentTime);

            String groupId = jsonObject.getString("groupId");

            String key = path + "_" + groupId;
            model.setKey(key);

            models.add(model);
        }
        Collections.sort(models, new Comparator<TxModel>() {
            @Override
            public int compare(TxModel o1, TxModel o2) {
                if (o2.getOrder() > o1.getOrder()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return models;
    }

    @Override
    public boolean hasCompensate() {
        return compensateDao.hasCompensate();
    }

    @Override
    public boolean delCompensate(String path) {
        compensateDao.deleteCompensateByPath(path);
        return true;
    }

    @Override
    public void reloadCompensate(TxGroup txGroup) {
        TxGroup compensateGroup = getCompensateByGroupId(txGroup.getGroupId());
        if (compensateGroup != null) {
            for (TxInfo txInfo : txGroup.getList()) {

                for (TxInfo cinfo : compensateGroup.getList()) {
                    if (cinfo.getModel().equals(txInfo.getModel()) && cinfo.getMethodStr().equals(txInfo.getMethodStr())) {

                        //根据之前的数据补偿现在的事务

                        int oldNotify = cinfo.getNotify();

                        if (oldNotify == 1) {
                            txInfo.setIsCommit(0);
                        } else {
                            txInfo.setIsCommit(1);
                        }
                    }
                }

            }
        }

        logger.info("加载补偿以后->"+JSON.toJSONString(txGroup));
    }

    private TxGroup getCompensateByGroupId(String groupId) {
        String json = compensateDao.getCompensateByGroupId(groupId);
        if (json == null) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(json);
        String txGroup = jsonObject.getString("txGroup");
        return JSON.parseObject(txGroup, TxGroup.class);
    }


    @Override
    public boolean executeCompensate(String path) throws ServiceException {

        String json = compensateDao.getCompensate(path);
        if (json == null) {
            throw new ServiceException("不存在该数据");
        }

        boolean hasOk = _executeCompensate(json);
        if (hasOk) {
            // 删除本地补偿数据
            compensateDao.deleteCompensateByPath(path);

            return true;
        }
        return false;
    }


    private boolean _executeCompensate(String json) throws ServiceException {
        JSONObject jsonObject = JSON.parseObject(json);

        String model = jsonObject.getString("model");

        ModelInfo modelInfo = ModelInfoManager.getInstance().getModelByModel(model);
        if (modelInfo == null) {
            throw new ServiceException("当前模块不在线.");
        }

        String data = jsonObject.getString("data");

        String groupId = jsonObject.getString("groupId");

        String res = managerSenderService.sendCompensateMsg(modelInfo.getChannelName(), groupId, data);

        return "1".equals(res);
    }
}
