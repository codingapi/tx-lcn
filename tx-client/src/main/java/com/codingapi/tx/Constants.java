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
     */
    public static final int ASPECT_ORDER = 1000;

}
