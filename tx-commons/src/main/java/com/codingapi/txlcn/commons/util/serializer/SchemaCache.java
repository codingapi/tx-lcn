package com.codingapi.txlcn.commons.util.serializer;

import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author lorne 2017/11/11
 */
public class SchemaCache {

    private static class SchemaCacheHolder {
        private static SchemaCache cache = new SchemaCache();
    }

    public static SchemaCache getInstance() {
        return SchemaCacheHolder.cache;
    }

    private Cache<Class<?>, Schema<?>> cache = CacheBuilder.newBuilder()
        .maximumSize(1024).expireAfterWrite(1, TimeUnit.HOURS)
        .build();

    private Schema<?> get(final Class<?> cls, Cache<Class<?>, Schema<?>> cache) {
        try {
            return cache.get(cls, () -> RuntimeSchema.createFrom(cls));

        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Schema<?> get(final Class<?> cls) {
        return get(cls, cache);
    }
}

