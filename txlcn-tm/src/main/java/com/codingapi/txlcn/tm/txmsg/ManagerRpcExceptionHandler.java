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
package com.codingapi.txlcn.tm.txmsg;

import com.codingapi.txlcn.tm.support.service.TxExceptionService;
import com.codingapi.txlcn.tm.support.restapi.ao.WriteTxExceptionDTO;
import com.codingapi.txlcn.txmsg.params.NotifyUnitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
@Component
@Slf4j
public class ManagerRpcExceptionHandler implements RpcExceptionHandler {

    private final TxExceptionService compensationService;

    @Autowired
    public ManagerRpcExceptionHandler(TxExceptionService compensationService) {
        this.compensationService = compensationService;
    }

    @Override
    public void handleNotifyUnitBusinessException(Object params, Throwable e) {
        // the same to message error
        handleNotifyUnitMessageException(params, e);
    }

    @Override
    public void handleNotifyUnitMessageException(Object params, Throwable e) {
        // notify unit message error, write txEx
        List paramList = ((List) params);
        String modName = (String) paramList.get(1);

        NotifyUnitParams notifyUnitParams = (NotifyUnitParams) paramList.get(0);
        WriteTxExceptionDTO writeTxExceptionReq = new WriteTxExceptionDTO(notifyUnitParams.getGroupId(),
                notifyUnitParams.getUnitId(), modName, notifyUnitParams.getState());
        writeTxExceptionReq.setRegistrar((short) 0);
        compensationService.writeTxException(writeTxExceptionReq);
    }
}
