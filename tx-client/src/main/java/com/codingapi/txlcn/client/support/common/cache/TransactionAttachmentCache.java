/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.client.support.common.cache;

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
     * @param groupId groupId
     * @param unitId unitId
     * @param attachment attachment
     */
    <T> void attach(String groupId, String unitId, T attachment);


    /**
     * 移除给定 groupId 所有相关的 attachments
     *
     * @param groupId groupId
     * @param unitId unitId
     */
    void removeAttachments(String groupId, String unitId);

    /**
     * 获取给定 groupId 相关联的、给定类型的 attachment
     *
     * @param groupId groupId
     * @param type type
     * @param <T> T
     * @return  Optional
     */
    <T> Optional<T> attachment(String groupId, Class<T> type);

    /**
     * 获取给定 groupId 相关联的、给定类型的 attachment
     * 不存在时缓存给定的 def 默认值并返回
     *
     * @param groupId groupId
     * @param  unitId unitId
     * @param type type
     * @param def     不存在时的默认值
     * @param <T> T
     * @return T
     */
    <T> T attachment(String groupId, String unitId, Class<T> type, Supplier<T> def);

    /**
     * 判断是否存在给定的 groupId 事务组
     *
     * @param groupId groupId
     * @return  hasGroup
     */
    boolean hasGroup(String groupId);

    /**
     * 判断事务组 groupId 是否存在给定类型 type 的附加对象 attachment
     *
     * @param groupId groupId
     * @param type type
     * @return hasAttachment
     */
    boolean hasAttachment(String groupId, Class<?> type);
}
