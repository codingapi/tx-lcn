package com.lorne.tx.compensate.dao.impl;

import com.alibaba.fastjson.JSON;
import com.lorne.core.framework.utils.DateUtil;
import com.lorne.core.framework.utils.config.ConfigUtils;
import com.lorne.tx.compensate.dao.CompensateDao;
import com.lorne.tx.compensate.model.TransactionCompensateMsg;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class CompensateDaoImpl implements CompensateDao{

    private String logPath;

    public CompensateDaoImpl() {
        logPath = ConfigUtils.getString("application.properties","tm.compensate.log.path");
    }

    @Override
    public boolean saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg) {
        String name =String.format("/%s/compensate_%s.json",transactionCompensateMsg.getModel(),DateUtil.getCurrentDateFormat());
        String json = JSON.toJSONString(transactionCompensateMsg);

        File file = new File(logPath+"/"+name);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }

        try {
            FileUtils.writeStringToFile(file,json,true);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
