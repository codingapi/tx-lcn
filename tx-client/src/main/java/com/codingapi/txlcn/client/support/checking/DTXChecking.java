package com.codingapi.txlcn.client.support.checking;

/**
 * Description:分布式事务检测器。未收到通知事务单元指令的超时处理机制
 * Date: 2018/12/19
 *
 * @author ujued
 */
public interface DTXChecking {

    /**
     * 开始事务检测。设置定时器，在超时时间后做最后事务状态的确认
     *
     * @param groupId
     * @param unitId
     * @param transactionType
     */
    void startDelayCheckingAsync(String groupId, String unitId, String transactionType);

    /**
     * 手动停止事务检测。确定分布式事务结果正常时手动结束检测
     *
     * @param groupId
     * @param unitId
     */
    void stopDelayChecking(String groupId, String unitId);
}
