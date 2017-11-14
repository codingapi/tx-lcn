package com.codingapi.tx.control.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.compensate.service.CompensateService;
import com.codingapi.tx.control.service.IActionService;
import com.codingapi.tx.framework.utils.SerializerUtils;
import com.codingapi.tx.model.TransactionInvocation;
import com.lorne.core.framework.utils.encode.Base64Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通知补偿
 * create by lorne on 2017/11/13
 */
@Service(value = "c")
public class ActionCServiceImpl implements IActionService {


    private Logger logger = LoggerFactory.getLogger(ActionCServiceImpl.class);


    @Autowired
    private CompensateService compensateService;

    @Override
    public String execute(JSONObject resObj, String json) {

        String cmd = resObj.toJSONString();

        logger.info("接受补偿->" + cmd);


        String data = resObj.getString("d");

        String groupId = resObj.getString("g");

        byte[] bytes = Base64Utils.decode(data);

        TransactionInvocation invocation = SerializerUtils.parserTransactionInvocation(bytes);

        if (invocation != null) {
            logger.info("接受补偿->" + invocation.getMethodStr());

            boolean res = compensateService.invoke(invocation, groupId);

            logger.info("补偿结果->" + res);

            if (res) {
                return "1";
            } else {
                return "0";
            }
        }

        return "-1";
    }


}
