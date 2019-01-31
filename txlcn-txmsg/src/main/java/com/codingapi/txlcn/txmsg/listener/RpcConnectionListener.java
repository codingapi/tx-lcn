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
package com.codingapi.txlcn.txmsg.listener;

/**
 * @author lorne 2019/1/31
 */
public interface RpcConnectionListener {

    /**
     * 建立连接监听
     * @param remoteKey 远程key
     */
    void connect(String remoteKey);

    /**
     * 断开连接监听
     * @param remoteKey 远程key
     * @param appName   模块名称
     */
    void disconnect(String remoteKey,String appName);

}
