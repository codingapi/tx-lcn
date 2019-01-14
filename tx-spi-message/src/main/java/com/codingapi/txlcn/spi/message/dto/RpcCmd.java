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
package com.codingapi.txlcn.spi.message.dto;

import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.spi.message.exception.RpcException;

import java.io.Serializable;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
public abstract class RpcCmd implements Serializable {
    
    /**
     * 指令唯一标识
     * 当存在key时需要对方相应指令
     */
    private String key;
    
    /**
     * 请求的消息内容体
     */
    private MessageDto msg;
    
    /**
     * 远程标识关键字
     */
    private String remoteKey;
    
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public MessageDto getMsg() {
        return msg;
    }
    
    public void setMsg(MessageDto msg) {
        this.msg = msg;
    }
    
    public String getRemoteKey() {
        return remoteKey;
    }
    
    public void setRemoteKey(String remoteKey) {
        this.remoteKey = remoteKey;
    }
    
    /**
     * get rpc result
     *@return  result
     */
    public abstract MessageDto loadResult() throws RpcException;
    
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
