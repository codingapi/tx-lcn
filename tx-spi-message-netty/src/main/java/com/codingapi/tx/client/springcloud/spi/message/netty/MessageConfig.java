package com.codingapi.tx.client.springcloud.spi.message.netty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 19-1-9 下午6:04
 *
 * @author ujued
 */
@NoArgsConstructor
@Data
public class MessageConfig {
    /**
     * 最大等待时间 单位:(s)
     */
    private int waitTime = 5;

    /**
     * 最大缓存锁的数量
     */
    private int cacheSize = 1024;

    /**
     * appName 参数延迟删除时间 单位：(s)
     */
    private int attrDelayTime = 10;

}
