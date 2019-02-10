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
package com.codingapi.txlcn.common.util.function;

/**
 * Description:
 * Date: 19-1-22 下午6:54
 *
 * @author ujued
 */
public interface Supplier<T, E extends Throwable> {

    /**
     * 获取 {@code T} 的实例
     *
     * @return T' implementation
     * @throws E ex
     */
    T get() throws E;
}
