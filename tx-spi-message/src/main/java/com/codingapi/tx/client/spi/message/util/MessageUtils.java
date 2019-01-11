package com.codingapi.tx.client.spi.message.util;

import com.codingapi.tx.client.spi.message.MessageConstants;
import com.codingapi.tx.client.spi.message.dto.MessageDto;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
public class MessageUtils {

    /**
     * 响应消息状态
     *
     * @param messageDto
     * @return
     */
    public static boolean statusOk(MessageDto messageDto) {
        return messageDto.getState() == MessageConstants.STATE_OK;
    }
}
