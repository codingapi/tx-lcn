package com.codingapi.txlcn.tm.repository;

import java.util.List;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020-9-13 23:12:57
 */
public interface TmNodeRepository {

    /**
     * 是否存在该 Key
     *
     * @param key key
     * @return Boolean
     */
    Boolean hasKey(String key);

    /**
     * 获取符合条件的key
     *
     * @param pattern key 正则
     * @return List<String>
     */
    List<String> keys(String pattern);

    /**
     * 获得 TM 信息
     *
     * @param key key
     * @return TmNodeInfo
     */
    TmNodeInfo getTmNodeInfo(String key);

    /**
     * 删除
     *
     * @param key key
     * @return Boolean
     */
    Boolean delete(String key);

    /**
     * 存在则设置
     *
     * @param tmNodeInfo TmNodeInfo
     * @param expireTime 存活时间
     * @return Boolean
     */
    Boolean setIfAbsent(TmNodeInfo tmNodeInfo, long expireTime);

    /**
     * 设置 String 类型
     *
     * @param tmNodeInfo TmNodeInfo
     * @param expireTime 存活时间
     */
    void set(TmNodeInfo tmNodeInfo, long expireTime);
}