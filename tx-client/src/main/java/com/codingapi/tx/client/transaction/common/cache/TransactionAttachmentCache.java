package com.codingapi.tx.client.transaction.common.cache;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Description: 事务组相关附加对象缓存，一个组对应多个附加对象，用对象类型作为标识。
 * 同时与事务组相关的缓存对象的生命周期同事务组一致
 * <p>
 * Date: 2018/12/3
 *
 * @author ujued
 */
public interface TransactionAttachmentCache {

    /**
     * 缓存组相关信息。需要注意的是，给定的 groupId 所关联的 attachment 类型不能相同
     * 相同类型的 attachment 只会保留最后那个
     *
     * @param groupId
     * @param unitId
     * @param attachment
     */
    <T> void attach(String groupId, String unitId, T attachment);


    /**
     * 移除给定 groupId 所有相关的 attachments
     *
     * @param groupId
     * @param unitId
     */
    void removeAttachments(String groupId, String unitId);

    /**
     * 获取给定 groupId 相关联的、给定类型的 attachment
     *
     * @param groupId
     * @param type
     * @param <T>
     * @return
     */
    <T> Optional<T> attachment(String groupId, Class<T> type);

    /**
     * 获取给定 groupId 相关联的、给定类型的 attachment
     * 不存在时缓存给定的 def 默认值并返回
     *
     * @param groupId
     * @param unitId
     * @param type
     * @param def     不存在时的默认值
     * @param <T>
     * @return
     */
    <T> T attachment(String groupId, String unitId, Class<T> type, Supplier<T> def);

    /**
     * 判断是否存在给定的 groupId 事务组
     *
     * @param groupId
     * @return
     */
    boolean hasGroup(String groupId);

    /**
     * 判断事务组 groupId 是否存在给定类型 type 的附加对象 attachment
     *
     * @param groupId
     * @param type
     * @return
     */
    boolean hasAttachment(String groupId, Class<?> type);
}
