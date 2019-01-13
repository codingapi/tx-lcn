package com.codingapi.tx.commons.context;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Date: 1/11/19
 *
 * @author ujued
 */
@Slf4j
public class MapBasedContext implements GenericContext {
    private Map<String, Content> context = new ConcurrentHashMap<>(16);
    private Map<String, Object> attributes = new ConcurrentHashMap<>(16);

    @Override
    public void hold(String id, String key, Object value) {
        if (Objects.isNull(id)) {
            log.warn("id can't be null.");
            return;
        }
        if (context.containsKey(id)) {
            MapBasedContent content = (MapBasedContent) context.get(id);
            content.getCache().put(key, value);
            content.getKeys().add(key);
            return;
        }
        MapBasedContent groupCache = new MapBasedContent();
        groupCache.getKeys().add(key);
        groupCache.getCache().put(key, value);
        context.put(id, groupCache);
    }

    @Override
    public Content content(String id) {
        return context.get(id);
    }

    @Override
    public <T> T valueOfContent(String id, String key) {
        Content content = context.get(id);
        if (Objects.isNull(content)) {
            throw new IllegalArgumentException("error id.");
        }
        return content.value(key);
    }

    @Override
    public void discard(String id) {
        context.remove(id);
    }

    @Override
    public void discard(String id, String key) {
        MapBasedContent groupCache = (MapBasedContent) context.get(id);
        if (Objects.isNull(groupCache)) {
            return;
        }
        groupCache.getKeys().remove(key);
        if (groupCache.getKeys().isEmpty()) {
            context.remove(id);
        }
    }

    public void addAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T attribute(String key) {
        return (T) attributes.get(key);
    }
}
