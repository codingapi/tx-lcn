package com.codingapi.tx.compensate.dao.impl;

import com.codingapi.tx.compensate.dao.CompensateInfoDao;
import com.codingapi.tx.compensate.model.CompensateInfo;
import com.codingapi.tx.config.ConfigReader;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;

/**
 * create by lorne on 2017/11/13
 */
@Repository
public class FileCompensateInfoDaoImpl implements CompensateInfoDao {


    private Logger logger = LoggerFactory.getLogger(CompensateInfoDao.class);

    @Autowired
    private ConfigReader configReader;

    @Override
    public void saveCompensateInfo(String json) {

        File file = new File(configReader.getCompensatePath() + "/compensate.json");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            FileUtils.writeStringToFile(file, json, true);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

    }
}
