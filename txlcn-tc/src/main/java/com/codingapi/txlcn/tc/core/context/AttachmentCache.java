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
package com.codingapi.txlcn.tc.core.context;

/**
 * Description: 维护{@code mainKey} 和 {@code key}相关的附加对象
 * Date: 19-1-23 下午12:03
 *
 * @author ujued
 */
public interface AttachmentCache {

    /**
     * {@code mainKey} 附加 {@code key} 类型的对象
     *
     * @param mainKey    mainKey
     * @param key        key
     * @param attachment attachment
     */
    void attach(String mainKey, String key, Object attachment);

    /**
     * {@code key} 附加一个对象
     *
     * @param key        key
     * @param attachment attachment
     */
    void attach(String key, Object attachment);

    /**
     * 获取{@code mainKey} {@code key}类型的附加对象
     *
     * @param mainKey mainKey
     * @param key     key
     * @param <T>     type
     * @return attachment
     */
    <T> T attachment(String mainKey, String key);

    /**
     * 获取{@code key}的附加对象
     *
     * @param key key
     * @param <T> type
     * @return attachment
     */
    <T> T attachment(String key);

    /**
     * 移除{@code mainKey} {@code key}类型的附加对象
     *
     * @param mainKey mainKey
     * @param key     key
     */
    void remove(String mainKey, String key);

    /**
     * 移除{@code mainKey}所有类型的附加对象
     *
     * @param mainKey mainKey
     */
    void removeAll(String mainKey);

    /**
     * 移除{@code key}的附加对象
     *
     * @param key key
     */
    void remove(String key);

    /**
     * {@code mainKey}是否存在{@code key}类型的附加对象
     *
     * @param mainKey mainKey
     * @param key     key
     * @return 是否存在
     */
    boolean containsKey(String mainKey, String key);

    /**
     * {@code key}是否存在附加对象
     *
     * @param key key
     * @return 是否存在
     */
    boolean containsKey(String key);
}
