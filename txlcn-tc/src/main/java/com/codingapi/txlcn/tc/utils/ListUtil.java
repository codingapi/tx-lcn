package com.codingapi.txlcn.tc.utils;

import java.util.List;

/**
 * @author Gz.
 * @description: list工具类
 * @date 2020-08-13 23:08:26
 */
public class ListUtil {

    public static <T> boolean isEmpty(List<T> list){
        return list == null || list.size() == 0;
    }
    public static <T> boolean isNotEmpty(List<T> list){
        return list != null && list.size() != 0;
    }
}
