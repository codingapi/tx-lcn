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
package com.codingapi.txlcn.txmsg;

import com.codingapi.txlcn.txmsg.dto.AppInfo;
import com.codingapi.txlcn.txmsg.dto.RpcResponseState;
import com.codingapi.txlcn.txmsg.loadbalance.RpcLoadBalance;
import com.codingapi.txlcn.txmsg.dto.MessageDto;
import com.codingapi.txlcn.txmsg.dto.RpcCmd;
import com.codingapi.txlcn.txmsg.exception.RpcException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
public abstract class RpcClient {

    @Autowired
    private RpcLoadBalance rpcLoadBalance;

    /**
     * 发送指令不需要返回数据，需要知道返回的状态
     *
     * @param rpcCmd 指令内容
     * @return 指令状态
     * @throws RpcException 远程调用请求异常
     */
    public abstract RpcResponseState send(RpcCmd rpcCmd) throws RpcException;


    /**
     * 发送指令不需要返回数据，需要知道返回的状态
     *
     * @param remoteKey 远程标识关键字
     * @param msg       指令内容
     * @return 指令状态
     * @throws RpcException 远程调用请求异常
     */
    public abstract RpcResponseState send(String remoteKey, MessageDto msg) throws RpcException;


    /**
     * 发送请求并获取响应
     *
     * @param rpcCmd 指令内容
     * @return 响应指令数据
     * @throws RpcException 远程调用请求异常
     */
    public abstract MessageDto request(RpcCmd rpcCmd) throws RpcException;


    /**
     * 发送请求并响应
     *
     * @param remoteKey 远程标识关键字
     * @param msg       请求内容
     * @return 相应指令数据
     * @throws RpcException 远程调用请求异常
     */
    public abstract MessageDto request(String remoteKey, MessageDto msg) throws RpcException;

    /**
     * 发送请求并获取响应
     *
     * @param remoteKey 远程标识关键字
     * @param msg       请求内容
     * @param timeout   超时时间
     * @return 响应消息
     * @throws RpcException 远程调用请求异常
     */
    public abstract MessageDto request(String remoteKey, MessageDto msg, long timeout) throws RpcException;


    /**
     * 获取一个远程标识关键字
     *
     * @return 远程标识关键字
     * @throws RpcException 远程调用请求异常
     */
    public String loadRemoteKey() throws RpcException {
        return rpcLoadBalance.getRemoteKey();
    }


    /**
     * 获取所有的远程连接对象
     *
     * @return 远程连接对象数组.
     */
    public abstract List<String> loadAllRemoteKey();


    /**
     * 获取模块远程标识
     *
     * @param moduleName 模块名称
     * @return 远程标识
     */
    public abstract List<String> remoteKeys(String moduleName);


    /**
     * 绑定模块名称
     *
     * @param remoteKey 远程标识
     * @param appName   应用名称
     * @param labelName  TC标识名称
     * @throws RpcException   RpcException
     */
    public abstract void bindAppName(String remoteKey, String appName,String labelName) throws RpcException;


    /**
     * 获取模块名称
     *
     * @param remoteKey 远程标识
     * @return 应用名称
     */
    public abstract String getAppName(String remoteKey);


    /**
     * 获取所有的模块信息
     *
     * @return 应用名称
     */
    public abstract List<AppInfo> apps();

}
