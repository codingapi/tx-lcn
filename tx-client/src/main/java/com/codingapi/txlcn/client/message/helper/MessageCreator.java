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

import com.codingapi.txlcn.spi.message.params.*;
import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.util.serializer.SerializerContext;
import com.codingapi.txlcn.spi.message.MessageConstants;
import com.codingapi.txlcn.spi.message.dto.MessageDto;

import java.util.Objects;

/**
 * @author lorne
 */
public class MessageCreator {


    private static byte[] serialize(Object obj) {
        try {
            return SerializerContext.getInstance().serialize(obj);
        } catch (SerializerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建事务组
     *
     * @param groupId groupId
     * @return MessageDto
     */
    public static MessageDto createGroup(String groupId) {
        MessageDto msg = new MessageDto();
        msg.setGroupId(groupId);
        msg.setAction(MessageConstants.ACTION_CREATE_GROUP);
        return msg;
    }

    /**
     * 加入事务组
     *
     * @param joinGroupParams joinGroupParams
     * @return MessageDto
     */
    public static MessageDto joinGroup(JoinGroupParams joinGroupParams) {
        MessageDto msg = new MessageDto();
        msg.setGroupId(joinGroupParams.getGroupId());
        msg.setAction(MessageConstants.ACTION_JOIN_GROUP);
        msg.setBytes(serialize(joinGroupParams));
        return msg;
    }

    /**
     * 关闭事务组
     *
     * @param notifyGroupParams notifyGroupParams
     * @return MessageDto
     */
    public static MessageDto notifyGroup(NotifyGroupParams notifyGroupParams) {
        MessageDto msg = new MessageDto();
        msg.setGroupId(notifyGroupParams.getGroupId());
        msg.setAction(MessageConstants.ACTION_NOTIFY_GROUP);
        msg.setBytes(serialize(notifyGroupParams));
        return msg;
    }

    /**
     * 通知事务单元成功
     *
     * @param message message
     * @param action action
     * @return MessageDto
     */
    public static MessageDto notifyUnitOkResponse(Object message,String action) {
        MessageDto messageDto = new MessageDto();
        messageDto.setAction(action);
        messageDto.setState(MessageConstants.STATE_OK);
        messageDto.setBytes(Objects.isNull(message) ? null : (message instanceof byte[] ? (byte[]) message : serialize(message)));
        return messageDto;
    }

    /**
     * 通知事务单元失败
     *
     * @param message message
     * @param action action
     * @return MessageDto
     */
    public static MessageDto notifyUnitFailResponse(Object message,String action) {
        MessageDto messageDto = new MessageDto();
        messageDto.setAction(action);
        messageDto.setState(MessageConstants.STATE_EXCEPTION);
        messageDto.setBytes(Objects.isNull(message) ? null : serialize(message));
        return messageDto;
    }

    /**
     * 询问事务状态指令
     *
     * @param groupId groupId
     * @param unitId unitId
     * @return MessageDto
     */
    public static MessageDto askTransactionState(String groupId, String unitId) {
        MessageDto messageDto = new MessageDto();
        messageDto.setGroupId(groupId);
        messageDto.setAction(MessageConstants.ACTION_ASK_TRANSACTION_STATE);
        messageDto.setBytes(serialize(new AskTransactionStateParams(groupId, unitId)));
        return messageDto;
    }

    /**
     * 写异常信息指令
     *
     * @param txExceptionParams txExceptionParams
     * @return MessageDto
     */
    public static MessageDto writeTxException(TxExceptionParams txExceptionParams) {
        MessageDto messageDto = new MessageDto();
        messageDto.setAction(MessageConstants.ACTION_WRITE_EXCEPTION);
        messageDto.setGroupId(txExceptionParams.getGroupId());
        messageDto.setBytes(serialize(txExceptionParams));
        return messageDto;
    }

    /**
     * 初始化客户端请求
     * @param  appName appName
     * @return MessageDto
     */
    public static MessageDto initClient(String appName) {
        InitClientParams initClientParams = new InitClientParams();
        initClientParams.setAppName(appName);
        MessageDto messageDto = new MessageDto();
        messageDto.setGroupId("INITCLIENTGROUPID");
        messageDto.setBytes(serialize(initClientParams));
        messageDto.setAction(MessageConstants.ACTION_INIT_CLIENT);
        return messageDto;
    }
}
