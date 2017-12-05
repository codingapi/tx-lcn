package com.codingapi.tx.control;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.framework.utils.SocketManager;
import com.codingapi.tx.model.Request;
import org.apache.commons.lang.StringUtils;

/**
 * create by lorne on 2017/12/5
 */
public class LCNTransactionAspectSupport {



    private static LCNTransactionAspectSupport instance = null;

    private LCNTransactionAspectSupport(){}

    public static LCNTransactionAspectSupport currentTransactionStatus() {
        if (instance == null) {
            synchronized (LCNTransactionAspectSupport.class) {
                if(instance==null){
                    instance = new LCNTransactionAspectSupport();
                }
            }
        }
        return instance;
    }


    public boolean setRollbackOnly(){
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        if(txTransactionLocal==null){
            return false;
        }

        if(StringUtils.isEmpty(txTransactionLocal.getGroupId())){
            return false;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("g", txTransactionLocal.getGroupId());
        Request request = new Request("rg", jsonObject.toString());
        String json =  SocketManager.getInstance().sendMsg(request);
        try {
            return Integer.parseInt(json)==1;
        }catch (Exception e){
            return false;
        }
    }

}
