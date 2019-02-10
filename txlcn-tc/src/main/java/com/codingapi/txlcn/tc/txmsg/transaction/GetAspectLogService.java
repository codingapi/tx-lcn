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
package com.codingapi.txlcn.tc.txmsg.transaction;

import com.codingapi.txlcn.tc.corelog.aspect.AspectLog;
import com.codingapi.txlcn.tc.corelog.aspect.AspectLogHelper;
import com.codingapi.txlcn.tc.txmsg.RpcExecuteService;
import com.codingapi.txlcn.tc.txmsg.TransactionCmd;
import com.codingapi.txlcn.tc.aspect.TransactionInfo;
import com.codingapi.txlcn.common.exception.SerializerException;
import com.codingapi.txlcn.common.exception.TxClientException;
import com.codingapi.txlcn.common.util.serializer.SerializerContext;
import com.codingapi.txlcn.txmsg.params.GetAspectLogParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/29
 *
 * @author ujued
 */
@Component("rpc_get-aspect-log")
public class GetAspectLogService implements RpcExecuteService {

    private final AspectLogHelper txLogHelper;

    @Autowired
    public GetAspectLogService(AspectLogHelper txLogHelper) {
        this.txLogHelper = txLogHelper;
    }

    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxClientException {
        try {
            GetAspectLogParams getAspectLogParams =transactionCmd.getMsg().loadBean(GetAspectLogParams.class);
            AspectLog txLog = txLogHelper.getTxLog(getAspectLogParams.getGroupId(), getAspectLogParams.getUnitId());
            if (Objects.isNull(txLog)) {
                throw new TxClientException("non exists aspect log.");
            }

            TransactionInfo transactionInfo = SerializerContext.getInstance().deSerialize(txLog.getBytes(), TransactionInfo.class);
            return transactionInfo.toJsonObject();
        } catch (SerializerException e) {
            throw new TxClientException(e);
        }
    }
}
