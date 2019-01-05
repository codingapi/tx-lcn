package com.codingapi.tx.spi.message.dto;

import com.alibaba.fastjson.JSON;
import com.codingapi.tx.spi.message.exception.RpcException;

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

    public abstract MessageDto loadResult() throws RpcException;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
