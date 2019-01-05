package com.codingapi.tx.spi.message;

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
    public static final String ACTION_CREATE_GROUP = "cg";

    /**
     * 加入事务组
     */
    public static final String ACTION_JOIN_GROUP = "atg";

    /**
     * 关闭事务组
     */
    public static final String ACTION_NOTIFY_GROUP = "clg";

    /**
     * 事务通知
     */
    public static final String ACTION_NOTIFY_UNIT = "nt";

    /**
     * 询问事务状态
     */
    public static final String ACTION_ASK_TRANSACTION_STATE = "ats";

    /**
     * 写补偿
     */
    public static final String ACTION_WRITE_COMPENSATION = "wc";

    /**
     * 响应成功消息
     */
    public static final String ACTION_RPC_OK = "rok";

    /**
     * 响应异常消息
     */
    public static final String ACTION_RPC_EXCEPTION = "rex";

    /**
     * 心态检测
     */
    public static final String ACTION_RPC_HEART = "h";

    /**
     * 通知建立连接
     */
    public static final String ACTION_NOTIFY_CONNECT = "nc";

    /**
     * 获取切面日志
     */
    public static final String ACTION_GET_ASPECT_LOG = "gal";
    /**
     * 初始化客户端
     */
    public static final String ACTION_INIT_CLIENT = "ic";

}
