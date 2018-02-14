package com.codingapi.tx.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.aop.bean.TxCompensateLocal;
import com.codingapi.tx.aop.bean.TxTransactionInfo;
import com.codingapi.tx.compensate.model.CompensateInfo;
import com.codingapi.tx.compensate.service.CompensateService;
import com.codingapi.tx.config.ConfigReader;
import com.codingapi.tx.framework.utils.SerializerUtils;
import com.codingapi.tx.framework.utils.SocketManager;
import com.codingapi.tx.listener.service.ModelNameService;
import com.codingapi.tx.model.Request;
import com.codingapi.tx.model.TxGroup;
import com.codingapi.tx.netty.service.MQTxManagerService;
import com.codingapi.tx.netty.service.TxManagerHttpRequestHelper;
import com.lorne.core.framework.utils.encode.Base64Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lorne on 2017/6/30.
 */
@Service
public class MQTxManagerServiceImpl implements MQTxManagerService {


    @Autowired
    private ModelNameService modelNameService;

    @Autowired
    private ConfigReader configReader;

    @Autowired
    private CompensateService compensateService;

    @Autowired
    private TxManagerHttpRequestHelper managerHelper;


    @Override
    public void createTransactionGroup(String groupId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("g", groupId);
        Request request = new Request("cg", jsonObject.toString());
        SocketManager.getInstance().sendMsg(request);
    }

    @Override
    public TxGroup addTransactionGroup(String groupId, String taskId, boolean isGroup, String methodStr) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("g", groupId);
        jsonObject.put("t", taskId);
        jsonObject.put("ms", methodStr);
        jsonObject.put("s", isGroup ? 1 : 0);
        Request request = new Request("atg", jsonObject.toString());
        String json =  SocketManager.getInstance().sendMsg(request);
        return TxGroup.parser(json);
    }


    @Override
    public int closeTransactionGroup(final String groupId, final int state) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("g", groupId);
        jsonObject.put("s", state);
        Request request = new Request("ctg", jsonObject.toString());
        String json =  SocketManager.getInstance().sendMsg(request);
        try {
            return Integer.parseInt(json);
        }catch (Exception e){
            return 0;
        }
    }


    @Override
    public void uploadModelInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("m", modelNameService.getModelName());
        jsonObject.put("i", modelNameService.getIpAddress());
        jsonObject.put("u", modelNameService.getUniqueKey());
        Request request = new Request("umi", jsonObject.toString());
        String json = SocketManager.getInstance().sendMsg(request);
    }

    @Override
    public int cleanNotifyTransaction(String groupId, String taskId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("g", groupId);
        jsonObject.put("t", taskId);
        Request request = new Request("ckg", jsonObject.toString());
        String json =  SocketManager.getInstance().sendMsg(request);
        try {
            return Integer.parseInt(json);
        }catch (Exception e){
            return -2;
        }
    }


    @Override
    public int cleanNotifyTransactionHttp(String groupId, String waitTaskId) {
        String url = configReader.getTxUrl() + "cleanNotifyTransactionHttp?groupId=" + groupId + "&taskId=" + waitTaskId;
        String clearRes = managerHelper.httpGet(url);
        if(clearRes==null){
            return -1;
        }
        return  clearRes.contains("true") ? 1 : 0;
    }


    @Override
    public String httpGetServer() {
        String url = configReader.getTxUrl() + "getServer";
        return managerHelper.httpGet(url);
    }

    @Override
    public void sendCompensateMsg(String groupId, long time, TxTransactionInfo info,int startError) {

        String modelName = modelNameService.getModelName();
        String uniqueKey = modelNameService.getUniqueKey();
        String address = modelNameService.getIpAddress();


        byte[] serializers =  SerializerUtils.serializeTransactionInvocation(info.getInvocation());
        String data = Base64Utils.encode(serializers);

        String className = info.getInvocation().getTargetClazz().getName();
        String methodStr = info.getInvocation().getMethodStr();
        long currentTime = System.currentTimeMillis();


        CompensateInfo compensateInfo = new CompensateInfo(currentTime, modelName, uniqueKey, data, methodStr, className, groupId, address, time,startError);

        String json = managerHelper.httpPost(configReader.getTxUrl() + "sendCompensateMsg", compensateInfo.toParamsString());

        compensateInfo.setResJson(json);

        //记录本地日志
        compensateService.saveLocal(compensateInfo);

    }
}
