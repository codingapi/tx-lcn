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
package com.codingapi.txlcn.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Date: 19-1-28 下午4:47
 *
 * @author ujued
 */
public abstract class Maps {

    public static <K, V> Map<K, V> newHashMap(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static <K, V> Map<K, V> newHashMap(K key1, V value1, K key2, V value2) {
        Map<K, V> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

    public static <K, V> Map<K, V> newHashMap(K key1, V value1, K key2, V value2, K key3, V value3) {
        Map<K, V> map = newHashMap(key1, value1, key2, value2);
        map.put(key3, value3);
        return map;
    }

    public static <K, V> Map<K, V> newHashMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
        Map<K, V> map = newHashMap(key1, value1, key2, value2, key3, value3);
        map.put(key4, value4);
        return map;
    }
}
