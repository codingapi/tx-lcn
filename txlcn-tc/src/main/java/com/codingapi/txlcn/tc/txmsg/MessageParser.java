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
package com.codingapi.txlcn.tc.txmsg;

import com.codingapi.txlcn.txmsg.LCNCmdType;
import com.codingapi.txlcn.txmsg.MessageConstants;
import com.codingapi.txlcn.txmsg.dto.RpcCmd;
import com.codingapi.txlcn.txmsg.params.NotifyUnitParams;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息解析器
 * @author lorne
 */

@Slf4j
public class MessageParser {



    public static TransactionCmd parser(RpcCmd rpcCmd) {
        TransactionCmd cmd = new TransactionCmd();
        cmd.setRequestKey(rpcCmd.getKey());
        cmd.setType(LCNCmdType.parserCmd(rpcCmd.getMsg().getAction()));
        cmd.setGroupId(rpcCmd.getMsg().getGroupId());

        if (rpcCmd.getMsg().getAction().equals(MessageConstants.ACTION_NOTIFY_UNIT)) {
            NotifyUnitParams notifyUnitParams = rpcCmd.getMsg().loadBean(NotifyUnitParams.class);
            cmd.setTransactionType(notifyUnitParams.getUnitType());
        }

        cmd.setMsg(rpcCmd.getMsg());
        return cmd;
    }


}
