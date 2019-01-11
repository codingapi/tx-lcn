package com.codingapi.tx.spi.message.netty;

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


    /**
     * 重连次数默认10次，注意超过12次将出现netty递归异常
     */
    private int tryConnectTime = 10;

    /**
     * 重连的间隔等待时间默认15(秒)
     */
    private int tryConnectWaitTime = 15;

}
