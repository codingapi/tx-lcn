package com.codingapi.tx.config;

import com.lorne.core.framework.utils.config.ConfigUtils;
import org.springframework.stereotype.Component;

/**
 * create by lorne on 2017/11/13
 */
@Component
public class ConfigReader {

    private String txUrl;

    private String compensatePath;

    public ConfigReader() {
        txUrl = ConfigUtils.getString("tx.properties", "url");
        if (txUrl.contains("/tx/manager/getServer")) {
            txUrl = txUrl.replace("getServer", "");
        }

        compensatePath = ConfigUtils.getString("tx.properties", "compensate.path");

    }


    public String getTxUrl() {
        return txUrl;
    }

    public String getCompensatePath() {
        return compensatePath;
    }
}
