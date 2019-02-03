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

import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * Date: 19-1-24 下午6:06
 *
 * @author ujued
 */
@Slf4j
public class DefaultClientInitCallback implements ClientInitCallBack {

    @Override
    public void connected(String remoteKey) {
        log.info("client {} connected", remoteKey);
    }

    @Override
    public void connectFail(String remoteKey) {
        log.info("client {} disconnected", remoteKey);
    }
}
