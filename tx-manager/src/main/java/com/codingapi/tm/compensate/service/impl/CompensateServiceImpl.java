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
import com.codingapi.tm.manager.service.TxManagerService;
import com.codingapi.tm.model.ModelInfo;
import com.codingapi.tm.model.ModelName;
import com.codingapi.tm.netty.model.TxGroup;
import com.codingapi.tm.netty.model.TxInfo;
import com.google.common.collect.Lists;
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
    private ConfigReader configReader;

    @Autowired
    private TxManagerSenderService managerSenderService;

    @Autowired
    private TxManagerService managerService;


    private Executor threadPool = Executors.newFixedThreadPool(20);

    @Override
    public boolean saveCompensateMsg(final TransactionCompensateMsg transactionCompensateMsg) {

        TxGroup txGroup =managerService.getTxGroup(transactionCompensateMsg.getGroupId());
        if (txGroup == null) {
            //仅发起方异常，其他模块正常
            txGroup = new TxGroup();
            txGroup.setNowTime(System.currentTimeMillis());
            txGroup.setGroupId(transactionCompensateMsg.getGroupId());
            txGroup.setIsCompensate(1);
        }else {
            managerService.deleteTxGroup(txGroup);
        }

        transactionCompensateMsg.setTxGroup(txGroup);

        final String json = JSON.toJSONString(transactionCompensateMsg);

        logger.info("Compensate->" + json);

        final String compensateKey = compensateDao.saveCompensateMsg(transactionCompensateMsg);

        //调整自动补偿机制，若开启了自动补偿，需要通知业务返回success，方可执行自动补偿
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String groupId = transactionCompensateMsg.getGroupId();
                    JSONObject requestJson = new JSONObject();
                    requestJson.put("action", "compensate");
                    requestJson.put("groupId", groupId);
                    requestJson.put("json", json);

                    String url = configReader.getCompensateNotifyUrl();
                    logger.error("Compensate Callback Address->" + url);
                    String res = HttpUtils.postJson(url, requestJson.toJSONString());
                    logger.error("Compensate Callback Result->" + res);
                    if (configReader.isCompensateAuto()) {
                        //自动补偿,是否自动执行补偿
                        if (res.contains("success") || res.contains("SUCCESS")) {
                            //自动补偿
                            autoCompensate(compensateKey, transactionCompensateMsg);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Compensate Callback Fails->" + e.getMessage());
                }
            }
        });

        return StringUtils.isNotEmpty(compensateKey);



    }

    @Override
    public void autoCompensate(final String compensateKey, TransactionCompensateMsg transactionCompensateMsg) {
        final String json = JSON.toJSONString(transactionCompensateMsg);
        logger.info("Auto Compensate->" + json);
        //自动补偿业务执行...
        final int tryTime = configReader.getCompensateTryTime();
        boolean autoExecuteRes = false;
        try {
            int executeCount = 0;
            autoExecuteRes = _executeCompensate(json);
            logger.info("Automatic Compensate Result->" + autoExecuteRes + ",json->" + json);
            while (!autoExecuteRes) {
                logger.info("Compensate Failure, Entering Compensate Queue->" + autoExecuteRes + ",json->" + json);
                executeCount++;
                if(executeCount==3){
                    autoExecuteRes = false;
                    break;
                }
                try {
                    Thread.sleep(tryTime * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                autoExecuteRes = _executeCompensate(json);
            }

            //执行成功删除数据
            if(autoExecuteRes) {
                compensateDao.deleteCompensateByKey(compensateKey);
            }

        }catch (Exception e){
            logger.error("Auto Compensate Fails,msg:"+e.getLocalizedMessage());
            //推送数据给第三方通知
            autoExecuteRes = false;
        }

        //执行补偿以后通知给业务方
        String groupId = transactionCompensateMsg.getGroupId();
        JSONObject requestJson = new JSONObject();
        requestJson.put("action","notify");
        requestJson.put("groupId",groupId);
        requestJson.put("resState",autoExecuteRes);

        String url = configReader.getCompensateNotifyUrl();
        logger.error("Compensate Result Callback Address->" + url);
        String res = HttpUtils.postJson(url, requestJson.toJSONString());
        logger.error("Compensate Result Callback Result->" + res);

    }



    @Override
    public List<ModelName> loadModelList() {
        List<String> keys =  compensateDao.loadCompensateKeys();

        Map<String,Integer> models = new HashMap<String, Integer>();

        for(String key:keys){
            if(key.length()>36){
                String name =  key.substring(11,key.length()-25);
                int v = 1;
                if(models.containsKey(name)){
                    v =  models.get(name)+1;
                }
                models.put(name,v);
            }
        }
        List<ModelName> names = new ArrayList<>();

        for(String key:models.keySet()){
            int v = models.get(key);
            ModelName modelName = new ModelName();
            modelName.setName(key);
            modelName.setCount(v);
            names.add(modelName);
        }
        return names;
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

            String key = path + ":" + groupId;
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

            if(compensateGroup.getList() != null && !compensateGroup.getList().isEmpty()){
                //引用集合 iterator，方便匹配后剔除列表
                Iterator<TxInfo> iterator = Lists.newArrayList(compensateGroup.getList()).iterator();
                for (TxInfo txInfo : txGroup.getList()) {
                    while (iterator.hasNext()) {
                        TxInfo cinfo = iterator.next();
                        if (cinfo.getModel().equals(txInfo.getModel()) && cinfo.getMethodStr().equals(txInfo.getMethodStr())) {
                            //根据之前的数据补偿现在的事务
                            int oldNotify = cinfo.getNotify();

                            if (oldNotify == 1) {
                                //本次回滚
                                txInfo.setIsCommit(0);
                            } else {
                                //本次提交
                                txInfo.setIsCommit(1);
                            }
                            //匹配后剔除列表
                            iterator.remove();
                            break;
                        }
                    }
                }
            }else{//当没有List数据只记录了补偿数据时，理解问仅发起方提交其他均回滚
                for (TxInfo txInfo : txGroup.getList()) {
                    //本次回滚
                    txInfo.setIsCommit(0);
                }
            }
        }
        logger.info("Compensate Loaded->"+JSON.toJSONString(txGroup));
    }

    public TxGroup getCompensateByGroupId(String groupId) {
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
            throw new ServiceException("no data existing");
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

        int startError = jsonObject.getInteger("startError");

        ModelInfo modelInfo = ModelInfoManager.getInstance().getModelByModel(model);
        if (modelInfo == null) {
            throw new ServiceException("current model offline.");
        }

        String data = jsonObject.getString("data");

        String groupId = jsonObject.getString("groupId");

        String res = managerSenderService.sendCompensateMsg(modelInfo.getChannelName(), groupId, data,startError);

        logger.debug("executeCompensate->"+json+",@@->"+res);

        return "1".equals(res);
    }
}
