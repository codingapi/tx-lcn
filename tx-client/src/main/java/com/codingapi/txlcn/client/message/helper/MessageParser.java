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
package com.codingapi.txlcn.client.message.helper;

import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.util.serializer.SerializerContext;
import com.codingapi.txlcn.spi.message.LCNCmdType;
import com.codingapi.txlcn.spi.message.MessageConstants;
import com.codingapi.txlcn.spi.message.dto.RpcCmd;
import com.codingapi.txlcn.spi.message.params.NotifyUnitParams;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息解析器
 * @author lorne
 */

@Slf4j
public class MessageParser {


    private static <T> T deserialize(byte[] data, Class<T> type) {
        try {
            return SerializerContext.getInstance().deSerialize(data, type);
        } catch (SerializerException e) {
            throw new RuntimeException(e);
        }
    }

    public static TransactionCmd parser(RpcCmd rpcCmd) {
        TransactionCmd cmd = new TransactionCmd();
        cmd.setRequestKey(rpcCmd.getKey());
        cmd.setType(LCNCmdType.parserCmd(rpcCmd.getMsg().getAction()));
        cmd.setGroupId(rpcCmd.getMsg().getGroupId());

        if (rpcCmd.getMsg().getAction().equals(MessageConstants.ACTION_NOTIFY_UNIT)) {
            NotifyUnitParams notifyUnitParams = deserialize(rpcCmd.getMsg().getBytes(), NotifyUnitParams.class);
            cmd.setTransactionType(notifyUnitParams.getUnitType());
        }

        cmd.setMsg(rpcCmd.getMsg());
        return cmd;
    }


}
