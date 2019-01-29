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

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Date: 19-1-23 下午12:04
 *
 * @author ujued
 */
@Component
public class MapBasedAttachmentCache implements AttachmentCache {

    private Map<String, Map<String, Object>> cache = new ConcurrentHashMap<>(64);

    @Override
    public void attach(String groupId, String key, Object attachment) {
        Objects.requireNonNull(groupId);
        Objects.requireNonNull(key);
        Objects.requireNonNull(attachment);

        if (cache.containsKey(groupId)) {
            Map<String, Object> map = cache.get(groupId);
            map.put(key, attachment);
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put(key, attachment);
        cache.put(groupId, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T attachment(String groupId, String key) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(groupId);

        if (cache.containsKey(groupId)) {
            if (cache.get(groupId).containsKey(key)) {
                return (T) cache.get(groupId).get(key);
            }
        }
        return null;
    }

    @Override
    public void remove(String groupId, String key) {
        if (cache.containsKey(groupId)) {
            cache.get(groupId).remove(key);
        }
    }

    @Override
    public boolean containsKey(String groupId, String key) {
        return cache.containsKey(groupId) && cache.get(groupId).containsKey(key);
    }

    @Override
    public void remove(String groupId) {
        this.cache.remove(groupId);
    }
}