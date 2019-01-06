package com.codingapi.tx.manager.support.message;

import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.util.serializer.SerializerContext;
import com.codingapi.tx.spi.message.MessageConstants;
import com.codingapi.tx.spi.message.params.NotifyConnectParams;
import com.codingapi.tx.spi.message.params.GetAspectLogParams;
import com.codingapi.tx.spi.message.dto.MessageDto;
import com.codingapi.tx.spi.message.params.NotifyUnitParams;

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
     * 通知TxClient连接
     *
     * @param notifyConnectParams
     * @return
     */
    public static MessageDto notifyConnect(NotifyConnectParams notifyConnectParams) {
        MessageDto msg = new MessageDto();
        msg.setAction(MessageConstants.ACTION_NEW_CONNECT);
        msg.setBytes(serialize(notifyConnectParams));
        return msg;
    }

    /**
     * 提交事务组
     *
     * @param notifyUnitParams
     * @return
     */
    public static MessageDto notifyUnit(NotifyUnitParams notifyUnitParams) {
        MessageDto msg = new MessageDto();
        msg.setGroupId(notifyUnitParams.getGroupId());
        msg.setAction(MessageConstants.ACTION_NOTIFY_UNIT);
        msg.setBytes(serialize(notifyUnitParams));
        return msg;
    }

    /**
     * 关闭事务组正常响应
     *
     * @return
     */
    public static MessageDto notifyGroupOkResponse(Object message,String action) {
        MessageDto messageDto = new MessageDto();
        messageDto.setState(MessageConstants.STATE_OK);
        messageDto.setAction(action);
        messageDto.setBytes(Objects.isNull(message) ? null : serialize(message));
        return messageDto;
    }

    /**
     * 关闭事务组失败响应
     *
     * @return
     */
    public static MessageDto notifyGroupFailResponse(Object message,String action) {
        MessageDto messageDto = new MessageDto();
        messageDto.setAction(action);
        messageDto.setState(MessageConstants.STATE_EXCEPTION);
        messageDto.setBytes(Objects.isNull(message) ? null : serialize(message));
        return messageDto;
    }

    /**
     * 服务器错误
     *
     * @return
     */
    public static MessageDto serverException(String action) {
        MessageDto messageDto = new MessageDto();
        messageDto.setAction(action);
        messageDto.setState(MessageConstants.STATE_EXCEPTION);
        messageDto.setBytes(serialize("Internal Server Error"));
        return messageDto;
    }

    public static MessageDto getAspectLog(String groupId, String unitId) {
        GetAspectLogParams getAspectLogParams = new GetAspectLogParams();
        getAspectLogParams.setGroupId(groupId);
        getAspectLogParams.setUnitId(unitId);

        MessageDto messageDto = new MessageDto();
        messageDto.setGroupId(groupId);
        messageDto.setAction(MessageConstants.ACTION_GET_ASPECT_LOG);
        messageDto.setBytes(serialize(getAspectLogParams));
        return messageDto;
    }
}
