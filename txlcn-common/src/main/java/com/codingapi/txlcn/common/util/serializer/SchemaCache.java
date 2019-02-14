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
package com.codingapi.txlcn.common.util.serializer;


import com.codingapi.txlcn.common.util.serializer.jdk.ListMultimapDelegate;
import com.codingapi.txlcn.common.util.serializer.jdk.MultimapDelegate;
import com.codingapi.txlcn.common.util.serializer.jdk.MultisetDelegate;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import io.protostuff.Schema;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.Delegate;
import io.protostuff.runtime.RuntimeEnv;
import io.protostuff.runtime.RuntimeSchema;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author lorne 2017/11/11
 */
public class SchemaCache {

    private static class SchemaCacheHolder {
        private static SchemaCache cache = new SchemaCache();
    }

    private final static DefaultIdStrategy idStrategy = ((DefaultIdStrategy) RuntimeEnv.ID_STRATEGY);

    //java.sql
    private final static Delegate<Timestamp> TIMESTAMP_DELEGATE = new TimestampDelegate();
    private final static Delegate<Date> DATE_DELEGATE = new DateDelegate();

    //google gauva
    private final static Delegate<ListMultimap> LISTMULTIMAP_DELEGATE = new ListMultimapDelegate();
    private final static Delegate<Multimap> MULTIMAP_DELEGATE = new MultimapDelegate();
    private final static Delegate<Multiset> MULTISET_DELEGATE = new MultisetDelegate();

    static {
        idStrategy.registerDelegate(TIMESTAMP_DELEGATE);
        idStrategy.registerDelegate(DATE_DELEGATE);
        idStrategy.registerDelegate(LISTMULTIMAP_DELEGATE);
        idStrategy.registerDelegate(MULTIMAP_DELEGATE);
        idStrategy.registerDelegate(MULTISET_DELEGATE);
    }

    /**
     * 注册 Delegate
     * @param delegate delegate
     */
    public void registerDelegate(Delegate<?> delegate){
        if(delegate!=null){
            idStrategy.registerDelegate(delegate);
        }
    }

    public static SchemaCache getInstance() {
        return SchemaCacheHolder.cache;
    }

    private Cache<Class<?>, Schema<?>> cache = CacheBuilder.newBuilder()
        .maximumSize(1024).expireAfterWrite(1, TimeUnit.HOURS)
        .build();

    private Schema<?> get(final Class<?> cls, Cache<Class<?>, Schema<?>> cache) {
        try {
            return cache.get(cls, () -> RuntimeSchema.createFrom(cls,idStrategy));
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Schema<?> get(final Class<?> cls) {
        return get(cls, cache);
    }
}

