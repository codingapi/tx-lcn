package com.codingapi.txlcn.tc.support.context;

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
