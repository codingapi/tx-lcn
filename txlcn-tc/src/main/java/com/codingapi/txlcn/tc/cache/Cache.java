package com.codingapi.txlcn.tc.cache;

import com.codingapi.txlcn.tc.constant.CommonConstant;
import com.codingapi.txlcn.tc.constant.SnowflakeConstant;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @author WhomHim
 * @description 缓存
 * @date Create in 2019/9/29 11:09
 */
@Slf4j
public class Cache {

    private static LoadingCache<String, Object> localCache = CacheBuilder.newBuilder()
            .initialCapacity(100)
            .maximumSize(10000)
            //指定时间内没有被创建覆盖，则指定时间过后，再次访问时，会去刷新该缓存，在新值没有到来之前，始终返回旧值
            .refreshAfterWrite(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, Object>() {
                @Override
                public String load(String s) {
                    return CommonConstant.NULL;
                }
            });

    /**
     * 获得缓存
     *
     * @param key   key
     * @param value value
     */
    public static void setKey(String key, Object value) {
        log.debug("==> Cache.setKey [key:{} value:{}]", key, value);
        localCache.put(key, value);
    }

    /**
     * 设置缓存
     *
     * @param key key
     * @return Object
     */
    public static Object getKey(String key) {
        try {
            Object value = localCache.get(key);
            if (CommonConstant.NULL.equals(value)) {
                return null;
            }
            return value;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 取得 GroupId
     *
     * @return GroupId
     */
    public static String getGroupId() {
        String groupId;
        try {
            groupId = (String) getKey(SnowflakeConstant.GROUP_ID);
        } catch (Exception e) {
            log.debug("Cache.getGroupId is null!");
            groupId = UUID.randomUUID().toString();
        }
        return groupId;
    }

    /**
     * 取得 LogId
     *
     * @return LogId
     */
    public static long getLogId() {
        long logId;
        try {
            logId = (long) getKey(SnowflakeConstant.LOG_ID);
        } catch (Exception e) {
            log.debug("Cache.getLogId is null!");
            logId = UUID.randomUUID().getLeastSignificantBits();
        }
        return logId;
    }

}
