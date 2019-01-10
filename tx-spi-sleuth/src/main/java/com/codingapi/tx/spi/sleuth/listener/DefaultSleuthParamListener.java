package com.codingapi.tx.spi.sleuth.listener;

import com.codingapi.tx.spi.sleuth.TracerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Slf4j
public class DefaultSleuthParamListener implements SleuthParamListener {

    @Autowired
    private TracerHelper tracerHelper;

    @Override
    public List<String> beforeBalance(String localKey) {
        String oldKey = tracerHelper.getAppList();
        String val = oldKey;
        if(oldKey==null){
            val = localKey;
            tracerHelper.createAppList(val);
        }else{
            if(!oldKey.contains(localKey)){
                val = oldKey+","+localKey;
                tracerHelper.createAppList(val);
            }
        }
        return Arrays.asList(val.split(","));
    }

    @Override
    public void afterNewBalance(String key) {
        String oldKey = tracerHelper.getAppList();
        tracerHelper.createAppList(oldKey+","+key);
    }
}
