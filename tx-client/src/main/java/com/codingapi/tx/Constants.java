package com.codingapi.tx;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
     * 最大模块超时时间（毫秒）
     */
    public static int maxOutTime = 10000;

    /**
     * 模块唯一标示
     */
    public static String uniqueKey;
    
    /**
     * 用于优化ribbon负载
     */
    public static Map<String, Object> cacheModelInfo = new ConcurrentHashMap<>();


}
