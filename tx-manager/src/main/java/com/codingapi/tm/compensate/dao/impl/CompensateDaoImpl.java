package com.codingapi.tm.compensate.dao.impl;

import com.alibaba.fastjson.JSON;
import com.codingapi.tm.compensate.dao.CompensateDao;
import com.codingapi.tm.compensate.model.TransactionCompensateMsg;
import com.codingapi.tm.config.ConfigReader;
import com.lorne.core.framework.utils.DateUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class CompensateDaoImpl implements CompensateDao {


    @Autowired
    private ConfigReader configReader;

    @Override
    public boolean saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg) {
        String name = String.format("/%s/%s/compensate_%s.json", transactionCompensateMsg.getModel(), transactionCompensateMsg.getAddress(), DateUtil.getCurrentDateFormat());
        String json = JSON.toJSONString(transactionCompensateMsg);

        File file = new File(configReader.getCompensateLogPath() + "/" + name);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }

        try {
            FileUtils.writeStringToFile(file, json + "\n", true);
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    @Override
    public List<String> loadModelList() {
        File file = new File(configReader.getCompensateLogPath());
        return Arrays.asList(file.list());
    }


    @Override
    public List<String> childModel(String model) {
        if (model.startsWith(".")) {
            return null;
        }
        File file = new File(configReader.getCompensateLogPath() + "/" + model);
        return Arrays.asList(file.list());
    }


    @Override
    public List<String> logFile(String path) {
        if (path.startsWith(".")) {
            return null;
        }
        File file = new File(configReader.getCompensateLogPath() + "/" + path);
        return Arrays.asList(file.list());
    }


    @Override
    public List<String> getLogs(String path) {
        if (path.startsWith(".")) {
            return null;
        }
        File file = new File(configReader.getCompensateLogPath() + "/" + path);
        try {
            return FileUtils.readLines(file);
        } catch (IOException e) {
            return null;
        }
    }
}
