package com.codingapi.tx.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.Constants;
import com.codingapi.tx.aop.bean.TxTransactionInfo;
import com.codingapi.tx.compensate.service.CompensateService;
import com.codingapi.tx.framework.utils.SerializerUtils;
import com.codingapi.tx.framework.utils.SocketManager;
import com.codingapi.tx.listener.model.Request;
import com.codingapi.tx.listener.model.TxGroup;
import com.codingapi.tx.listener.service.ModelNameService;
import com.codingapi.tx.netty.service.MQTxManagerService;
import com.lorne.core.framework.utils.config.ConfigUtils;
import com.lorne.core.framework.utils.encode.Base64Utils;
import com.lorne.core.framework.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lorne on 2017/6/30.
 */
@Service
public class MQTxManagerServiceImpl implements MQTxManagerService {



    private Logger logger = LoggerFactory.getLogger(MQTxManagerServiceImpl.class);

    @Autowired
    private ModelNameService modelNameService;

    private String url;

    @Autowired
    private CompensateService compensateService;


    public MQTxManagerServiceImpl() {
        url = ConfigUtils.getString("tx.properties", "url");
        if(url.contains("/tx/manager/getServer")){
            url = url.replace("getServer","");
        }
    }

    @Override
    public TxGroup createTransactionGroup() {
        JSONObject jsonObject = new JSONObject();
        Request request = new Request("cg", jsonObject.toString());
        String json = SocketManager.getInstance().sendMsg(request);
        return TxGroup.parser(json);
    }

    @Override
    public TxGroup addTransactionGroup(String groupId, String taskId, boolean isGroup) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("g", groupId);
        jsonObject.put("t", taskId);
        jsonObject.put("u", Constants.uniqueKey);
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
        logger.info("closeTransactionGroup-res-"+groupId+"->" + json);
        try {
            return Integer.parseInt(json);
        }catch (Exception e){
            return 0;
        }
    }


    @Override
    public int checkTransactionInfo(String groupId, String taskId) {
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
    public int getTransaction(String groupId, String waitTaskId) {
        String json = HttpUtils.get(url + "getTransaction?groupId=" + groupId + "&taskId=" + waitTaskId);
        System.out.println("getTransaction-->groupId:"+groupId+",taskId:"+waitTaskId+",res:"+json);

        if (json == null) {
            return -2;
        }
        json = json.trim();
        try {
            return Integer.parseInt(json);
        }catch (Exception e){
            return -2;
        }
    }


    @Override
    public int clearTransaction(String groupId, String waitTaskId, boolean isGroup) {
        String murl = url + "clearTransaction?groupId=" +groupId + "&taskId=" + waitTaskId+"&isGroup="+(isGroup?1:0);
        String clearRes = HttpUtils.get(murl);
        if(clearRes==null){
            return -1;
        }
        return  clearRes.contains("true") ? 1 : 0;
    }


    @Override
    public String httpGetServer() {
        String murl = url + "getServer";
        return HttpUtils.get(murl);
    }


    @Override
    public void sendCompensateMsg(String groupId, long time, TxTransactionInfo info) {

        String modelName = modelNameService.getModelName();
        String uniqueKey = modelNameService.getUniqueKey();
        String address = modelNameService.getIpAddress();

        byte[] serializers =  SerializerUtils.serializeTransactionInvocation(info.getInvocation());
        String data = Base64Utils.encode(serializers);

        String method = info.getInvocation().getMethod();
        String className = info.getInvocation().getTargetClazz().getName();

        String postParam = "model="+modelName+"&uniqueKey="+uniqueKey+"" +
            "&address=" + address + "" +
            "&data="+data+"&time="+time+"&groupId="+groupId+"" +
            "&method="+method+"&className="+className;


        String json = HttpUtils.post(url + "sendCompensateMsg",postParam);
        //记录本地日志
        compensateService.saveLocal(modelName,uniqueKey,data,method,className,json);

    }
}
