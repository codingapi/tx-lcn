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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Description: 基于JDK线程安全的 {@code ConcurrentHashMap} 实现的 {@code TransactionAttachmentCache}
 * Date: 2018/12/3
 *
 * @author ujued
 * @see TransactionAttachmentCache
 */
@Component
@Slf4j
public class MapBasedTransactionAttachmentCache implements TransactionAttachmentCache {

    class GroupCache {
        private Map<String, Object> cache = new HashMap<>();
        private Set<String> units = new HashSet<>();

        public Map<String, Object> getCache() {
            return cache;
        }

        Set<String> getUnits() {
            return units;
        }

        @Override
        public String toString() {
            return "GroupCache{" +
                    "cache=" + cache +
                    ", units=" + units +
                    '}';
        }
    }

    /**
     * 线程安全的Cache
     */
    private Map<String, GroupCache> transactionInfoMap = new ConcurrentHashMap<>(16);


    @Override
    public <T> void attach(String groupId, String unitId, T attachment) {
        if (Objects.isNull(groupId)) {
            log.warn("GroupId is null!");
            return;
        }
        if (transactionInfoMap.containsKey(groupId)) {
            GroupCache groupCache = transactionInfoMap.get(groupId);
            groupCache.getCache().put(attachment.getClass().getName(), attachment);
            groupCache.getUnits().add(unitId);
            return;
        }
        GroupCache groupCache = new GroupCache();
        groupCache.getUnits().add(unitId);
        groupCache.getCache().put(attachment.getClass().getName(), attachment);
        transactionInfoMap.put(groupId, groupCache);
    }

    @Override
    public void removeAttachments(String groupId, String unitId) {
        GroupCache groupCache = transactionInfoMap.get(groupId);
        if (Objects.isNull(groupCache)) {
            return;
        }
        groupCache.getUnits().remove(unitId);
        if (groupCache.getUnits().size() == 0) {
            transactionInfoMap.remove(groupId);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> attachment(String groupId, Class<T> type) {
        if (Objects.isNull(groupId)) {
            log.warn("GroupId id null, so non attachment [{}]!", type);
            return Optional.empty();
        }
        GroupCache groupCache = transactionInfoMap.get(groupId);
        return (Optional<T>) Optional.ofNullable(Objects.isNull(groupCache) ? null : groupCache.getCache().get(type.getName()));
    }

    @Override
    public <T> T attachment(String groupId, String unitId, Class<T> type, Supplier<T> def) {
        Optional<T> optionalT = attachment(groupId, type);
        if (optionalT.isPresent()) {
            return optionalT.get();
        }
        attach(groupId, unitId, def.get());
        return attachment(groupId, unitId, type, def);
    }

    @Override
    public boolean hasGroup(String groupId) {
        if (Objects.isNull(groupId)) {
            log.warn("GroupId is null!");
            return false;
        }
        return transactionInfoMap.containsKey(groupId);
    }

    @Override
    public boolean hasAttachment(String groupId, Class<?> type) {
        return hasGroup(groupId) && transactionInfoMap.get(groupId).getCache().containsKey(type.getName());
    }
}
