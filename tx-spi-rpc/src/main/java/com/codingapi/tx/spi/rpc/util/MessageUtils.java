package com.codingapi.tx.spi.rpc.util;

import com.codingapi.tx.spi.rpc.dto.MessageDto;
import com.codingapi.tx.spi.rpc.MessageConstants;

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
        return messageDto.getAction().equalsIgnoreCase(MessageConstants.ACTION_RPC_OK);
    }
}
