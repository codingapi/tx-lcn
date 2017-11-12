package com.codingapi.tm.compensate.dao.impl;

import com.alibaba.fastjson.JSON;
import com.codingapi.tm.compensate.dao.CompensateDao;
import com.codingapi.tm.compensate.model.TransactionCompensateMsg;
import com.lorne.core.framework.utils.DateUtil;
import com.lorne.core.framework.utils.config.ConfigUtils;
import org.apache.commons.io.FileUtils;
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

    private String logPath;

    public CompensateDaoImpl() {
        logPath = ConfigUtils.getString("application.properties","tm.compensate.log.path");
    }

    @Override
    public boolean saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg) {
        String name = String.format("/%s/%s/compensate_%s.json", transactionCompensateMsg.getModel(), transactionCompensateMsg.getAddress(), DateUtil.getCurrentDateFormat());
        String json = JSON.toJSONString(transactionCompensateMsg);

        File file = new File(logPath+"/"+name);
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
        File file = new File(logPath);
        return Arrays.asList(file.list());
    }


    @Override
    public List<String> childModel(String model) {
        if (model.startsWith(".")) {
            return null;
        }
        File file = new File(logPath + "/" + model);
        return Arrays.asList(file.list());
    }


    @Override
    public List<String> logFile(String path) {
        if (path.startsWith(".")) {
            return null;
        }
        File file = new File(logPath + "/" + path);
        return Arrays.asList(file.list());
    }


    @Override
    public List<String> getLogs(String path) {
        if (path.startsWith(".")) {
            return null;
        }
        File file = new File(logPath + "/" + path);
        try {
            return FileUtils.readLines(file);
        } catch (IOException e) {
            return null;
        }
    }
}
