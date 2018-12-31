package com.codingapi.tx.netty.service;

/**
 * @author yizhishang
 */
public interface MQTxManagerFeginService {

    /**
     * 检查并清理事务数据
     * @param groupId   事务组id
     * @param waitTaskId    任务id
     * @return  事务状态
     */
    String cleanNotifyTransactionHttp(String groupId, String waitTaskId);

    /**
     * 记录补偿事务数据到tm
     * @param currentTime  时间
     * @param groupId    事务组id
     * @param model     模块名称
     * @param address 模块地址
     * @param uniqueKey 唯一标示
     * @param className 事务启动类
     * @param methodStr    事务启动方法
     * @param data      切面数据
     * @param time      执行时间
     * @param startError 启动模块异常
     * @return  是否保存成功
     */
    String sendCompensateMsg(long currentTime, String groupId, String model, String address, String uniqueKey, String className, String methodStr, String data, long time,int startError);

    /**
     * 获取TM服务地址
     * @return txServer
     */
    String getServer();

}
