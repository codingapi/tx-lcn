package com.codingapi.txlcn.manager.core.message;


import com.codingapi.txlcn.spi.message.LCNCmdType;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import lombok.Data;

/**
 * @author lorne
 * @date 2018/12/2
 * @description
 */
@Data
public class TransactionCmd {

    /**
     * 业务状态
     */
    private LCNCmdType type;

    /**
     * 请求唯一标识
     */
    private String requestKey;

    /**
     * 事务组id
     */
    private String groupId;

    /**
     * TxClient标识键
     */
    private String remoteKey;

    /**
     * 通讯数据
     */
    private MessageDto msg;

}
