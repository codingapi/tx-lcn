package com.codingapi.txlcn.spi.message;

/**
 * Description:
 * Date: 2018/12/6
 *
 * @author ujued
 */
public class MessageConstants {

    /**
     * 创建事务组
     */
    public static final String ACTION_CREATE_GROUP = "createGroup";

    /**
     * 加入事务组
     */
    public static final String ACTION_JOIN_GROUP = "joinGroup";

    /**
     * 关闭事务组
     */
    public static final String ACTION_NOTIFY_GROUP = "notifyGroup";

    /**
     * 事务通知
     */
    public static final String ACTION_NOTIFY_UNIT = "notifyUnit";

    /**
     * 询问事务状态
     */
    public static final String ACTION_ASK_TRANSACTION_STATE = "askTransactionState";

    /**
     * 写异常记录
     */
    public static final String ACTION_WRITE_EXCEPTION = "writeException";

    /**
     * 心态检测
     */
    public static final String ACTION_HEART_CHECK = "heartCheck";

    /**
     * 通知建立连接
     */
    public static final String ACTION_NEW_TXMANAGER = "newTxManager";

    /**
     * 获取切面日志
     */
    public static final String ACTION_GET_ASPECT_LOG = "getAspectLog";
    /**
     * 初始化客户端
     */
    public static final String ACTION_INIT_CLIENT = "initClient";




    /**
     * 发起请求状态
     */
    public static final int STATE_REQUEST = 100;

    /**
     * 响应成功状态
     */
    public static final int STATE_OK = 200;

    /**
     * 响应异常状态
     */
    public static final int STATE_EXCEPTION = 500;


}
