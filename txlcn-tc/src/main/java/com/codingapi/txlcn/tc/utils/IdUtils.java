package com.codingapi.txlcn.tc.utils;

import java.util.UUID;

/**
 * @author lorne
 * @date 2020/8/8
 * @description
 */
public class IdUtils {


    /**
     * 创建GroupId策略
     *
     * @return
     */
    public static String generateGroupId(){
        return UUID.randomUUID().toString();
    }


}
