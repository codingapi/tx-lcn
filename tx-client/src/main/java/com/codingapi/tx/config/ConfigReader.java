package com.codingapi.tx.config;

import com.lorne.core.framework.utils.config.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * create by lorne on 2017/11/13
 */
@Component
public class ConfigReader {

    private String txUrl;

    private String configName = "tx.properties";

    private String configKey = "url";

    private Logger logger = LoggerFactory.getLogger(ConfigReader.class);


    public ConfigReader() {
        loadConfig();
    }

    private void loadConfig(){
        try {
            txUrl = ConfigUtils.getString(configName, configKey);

            //兼容3.0的配置地址
            if (txUrl.contains("/tx/manager/getServer")) {
                txUrl = txUrl.replace("getServer", "");
            }

            //添加后缀/
            if (!txUrl.endsWith("/")){
                txUrl+="/";
            }

        }catch (Exception e){
            logger.error(e.getLocalizedMessage());
        }
    }

    /**
     * 重新设置配置文件名称
     * @param configName  配置文件名称
     * @param key  配置文件key值
     */
    public void setConfigName(String configName,String key) {
        this.configName = configName;
        this.configKey = key;
        loadConfig();
    }

    /**
     * 设置TxManager服务地址 格式如 http://127.0.0.1:8899/tx/manager/
     * @param txUrl
     */
    public void setTxUrl(String txUrl) {
        this.txUrl = txUrl;
    }


    public String getTxUrl() {
        return txUrl;
    }


}
