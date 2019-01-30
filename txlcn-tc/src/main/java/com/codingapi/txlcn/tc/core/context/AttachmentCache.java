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
 * Description:
 * Date: 19-1-23 下午12:03
 *
 * @author ujued
 */
public interface AttachmentCache {

    void attach(String mainKey, String key, Object attachment);

    void attach(String key, Object attachment);

    <T> T attachment(String mainKey, String key);

    <T> T attachment(String key);

    void remove(String mainKey, String key);

    void removeAll(String mainKey);

    void remove(String key);

    boolean containsKey(String groupId, String key);
}
