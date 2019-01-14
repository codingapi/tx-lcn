package com.codingapi.txlcn.client.message.helper;

import com.codingapi.txlcn.spi.message.params.*;
import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.util.serializer.SerializerContext;
import com.codingapi.txlcn.spi.message.MessageConstants;
import com.codingapi.txlcn.spi.message.dto.MessageDto;

import java.util.Objects;

/**
 * @author lorne
 * @date 2018/12/2
 * @description 消息创建器
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
     * @param groupId
     * @return
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
     * @param joinGroupParams
     * @return
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
     * @param notifyGroupParams
     * @return
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
     * @param message
     * @return
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
     * @param message
     * @return
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
     * @param groupId
     * @param unitId
     * @return
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
     * @param txExceptionParams
     * @return
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
     * @return
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
