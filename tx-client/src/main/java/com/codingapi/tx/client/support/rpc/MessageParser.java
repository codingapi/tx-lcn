package com.codingapi.tx.client.support.rpc;

import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.util.serializer.SerializerContext;
import com.codingapi.tx.spi.rpc.LCNCmdType;
import com.codingapi.tx.spi.rpc.MessageConstants;
import com.codingapi.tx.spi.rpc.dto.RpcCmd;
import com.codingapi.tx.spi.rpc.params.NotifyUnitParams;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 * @date 2018/12/2
 * @description 消息解析器
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
