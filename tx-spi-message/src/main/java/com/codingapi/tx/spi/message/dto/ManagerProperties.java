package com.codingapi.tx.spi.message.dto;

import lombok.Data;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/21
 *
 * @author codingapi
 */
@Data
public class ManagerProperties  {

    /**
     * 端口
     */
    private int rpcPort;

    /**
     * 心态检测时间
     */
    private int checkTime;


    /**
     * 其他参数
     */
    private transient Object params;

}
