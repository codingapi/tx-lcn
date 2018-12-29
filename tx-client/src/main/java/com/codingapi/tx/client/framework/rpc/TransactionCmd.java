package com.codingapi.tx.client.framework.rpc;


import com.codingapi.tx.commons.rpc.LCNCmdType;
import com.codingapi.tx.spi.rpc.dto.MessageDto;
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
     * 事务类型
     */
    private String transactionType;

    /**
     * 通讯数据
     */
    private MessageDto msg;

}
