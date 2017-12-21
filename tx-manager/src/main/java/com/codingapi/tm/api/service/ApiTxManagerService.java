package com.codingapi.tm.api.service;

import com.codingapi.tm.model.TxServer;
import com.codingapi.tm.model.TxState;

/**
 * Created by lorne on 2017/7/1.
 */
public interface ApiTxManagerService {


    /**
     * 获取本服务的信息
     *
     * @return 服务信息
     */
    TxServer getServer();


    /**
     * 给 tx模块发送指令
     * @param model 模块名称
     * @param msg   指令
     * @return 指令返回结果
     */
    String sendMsg(String model, String msg);

    /**
     * 检查并清理事务数据
     * @param groupId   事务组Id
     * @param taskId    任务Id
     * @param isGroup   是否合并事务
     * @return  事务状态
     */
    int cleanNotifyTransaction(String groupId, String taskId);


    /**
     * 保存事务补偿日志信息
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
    boolean sendCompensateMsg(long currentTime, String groupId, String model, String address, String uniqueKey, String className, String methodStr, String data, int time,int startError);

    /**
     * 获取服务器状态
     *
     * @return 状态
     */
    TxState getState();

}
