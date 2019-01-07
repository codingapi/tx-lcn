package com.codingapi.tx.spi.message.params;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@Data
public class InitClientParams implements Serializable {

    /**
     * 模块名称
     */
    private String appName;

    /**
     * 分布式事务执行最大时间
     */
    private long dtxTime;



}
