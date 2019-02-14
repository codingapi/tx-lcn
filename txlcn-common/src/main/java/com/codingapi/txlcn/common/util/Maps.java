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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static <K, V> Map<K, V> of(K key1, V value1) {
        ImmutableMap<K, V> map = new ImmutableMap<>();
        map.put(key1, value1);
        return map;
    }

    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2) {
        ImmutableMap<K, V> map = new ImmutableMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3) {
        Map<K, V> map = of(key1, value1, key2, value2);
        map.put(key3, value3);
        return map;
    }

    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
        Map<K, V> map = of(key1, value1, key2, value2, key3, value3);
        map.put(key3, value4);
        return map;
    }


    static class ImmutableMap<K, V> implements Map<K, V> {

        private K key1;

        private V value1;

        private K key2;

        private V value2;

        private K key3;

        private V value3;

        private K key4;

        private V value4;

        private int size;

        private int maxSize = 4;

        ImmutableMap() {

        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public boolean containsKey(Object o) {
            return Objects.nonNull(key1) && key1.equals(o) ||
                    Objects.nonNull(key2) && key2.equals(o) ||
                    Objects.nonNull(key3) && key3.equals(o) ||
                    Objects.nonNull(key4) && key4.equals(o);
        }

        @Override
        public boolean containsValue(Object o) {
            return Objects.nonNull(value1) && value1.equals(o) ||
                    Objects.nonNull(value2) && value2.equals(o) ||
                    Objects.nonNull(value3) && value3.equals(o) ||
                    Objects.nonNull(value4) && value4.equals(o);
        }

        @Override
        public V get(Object o) {
            if (!containsKey(o)) {
                return null;
            }
            return Objects.nonNull(key1) && key1.equals(o) ? value1 :
                    (Objects.nonNull(key2) && key2.equals(o) ? value2 :
                            (Objects.nonNull(key3) && key3.equals(o) ? value3 :
                                    (Objects.nonNull(key4) && key4.equals(o) ? value4 : null)));
        }

        @Override
        public V put(K k, V v) {
            if (Objects.isNull(key1)) {
                key1 = k;
                value1 = v;
                size++;
                return v;
            } else if (Objects.isNull(key2)) {
                key2 = k;
                value2 = v;
                size++;
                return v;
            } else if (Objects.isNull(key3)) {
                key3 = k;
                value3 = v;
                size++;
                return v;
            } else if (Objects.isNull(key4)) {
                key4 = k;
                value4 = v;
                size++;
                return v;
            }
            int index = keyIndex(k);
            if (index != -1) {
                V oldV = getValueByKeyIndex(index);
                setValueByKeyIndex(index, v);
                return oldV;
            }
            throw new IllegalStateException("ImmutableMap is full.");
        }

        @Override
        @SuppressWarnings("unchecked")
        public V remove(Object o) {
            if (containsKey(o)) {
                int index = keyIndex((K) o);
                V oldV = getValueByKeyIndex(index);
                setValueByKeyIndex(index, null);
                size--;
                return oldV;
            }
            return null;
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> map) {
            Objects.requireNonNull(map);
            int i = 0;
            for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
                if (++i > maxSize) {
                    break;
                }
                put(entry.getKey(), entry.getValue());
            }

        }

        @Override
        public void clear() {
            size = 0;
            key1 = null;
            value1 = null;
            key2 = null;
            value2 = null;
            key3 = null;
            value3 = null;
            key4 = null;
            value4 = null;
        }

        @Override
        public Set<K> keySet() {
            return Stream.of(key1, key2, key3, key4)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }

        @Override
        public Collection<V> values() {
            return Stream.of(value1, value2, value3, value4)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }

        @Override
        public Set<Entry<K, V>> entrySet() {
            int size = size();
            Set<Entry<K, V>> entries = new HashSet<>(maxSize);
            if (Objects.nonNull(key1)) {
                entries.add(new SimpleEntry<>(this, key1));
            }
            if (Objects.nonNull(key2)) {
                entries.add(new SimpleEntry<>(this, key2));
            }
            if (Objects.nonNull(key3)) {
                entries.add(new SimpleEntry<>(this, key3));
            }
            if (Objects.nonNull(key4)) {
                entries.add(new SimpleEntry<>(this, key4));
            }
            return entries;
        }

        private int keyIndex(K o) {
            return Objects.nonNull(key1) && key1.equals(o) ? 1 :
                    (Objects.nonNull(key2) && key2.equals(o) ? 2 :
                            (Objects.nonNull(key3) && key3.equals(o) ? 3 :
                                    (Objects.nonNull(key4) && key4.equals(o) ? 4 : -1)));
        }

        private void setValueByKeyIndex(int index, V value) {
            if (index == 1) {
                this.value1 = value;
            } else if (index == 2) {
                this.value2 = value;
            } else if (index == 3) {
                this.value3 = value;
            } else if (index == 4) {
                this.value4 = value;
            }
        }

        private V getValueByKeyIndex(int index) {
            if (index == 1) {
                return this.value1;
            } else if (index == 2) {
                return this.value2;
            } else if (index == 3) {
                return this.value3;
            } else if (index == 4) {
                return this.value4;
            }
            return null;
        }

        static class SimpleEntry<K, V> implements Map.Entry<K, V> {

            private ImmutableMap<K, V> map;

            private K key;

            public SimpleEntry(ImmutableMap<K, V> map, K key) {
                this.map = map;
                this.key = key;
            }

            @Override
            public K getKey() {
                return key;
            }

            @Override
            public V getValue() {
                return map.get(key);
            }

            @Override
            public V setValue(V value) {
                V old = getValue();
                map.put(key, value);
                return old;
            }
        }
    }
}
