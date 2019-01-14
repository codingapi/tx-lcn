/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.spi.sleuth.listener;

import com.codingapi.txlcn.spi.sleuth.TracerHelper;
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
