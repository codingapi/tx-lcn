package com.codingapi.tx;

import com.codingapi.tx.model.TxServer;

/**
 * Created by lorne on 2017/6/8.
 */
public class Constants {


    public static volatile boolean hasExit = false;

    /**
     * tm 服务对象
     */
    public static TxServer txServer;

    /**
     * 主切面的 order值
     * 主切面一定要在 @Transaction 切面的外层（ASPECT_ORDER 小于 <tx:annotation-driven>标签中的order ）
     * 主切面需要能接受到异常。接收到异常才会触发回滚
     * 这意味着自定义的切面若catch了异常且不向外传递，那么这个切面需要在主切面的外层（自定义切面order 小于 ASPECT_ORDER）
     */
    public static final int ASPECT_ORDER = 1000;

}
