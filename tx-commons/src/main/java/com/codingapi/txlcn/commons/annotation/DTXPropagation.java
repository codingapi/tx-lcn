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
package com.codingapi.txlcn.commons.annotation;

/**
 * Description:
 * Date: 19-1-11 下午4:21
 *
 * @author ujued
 */
public enum DTXPropagation {
    /**
     * 当前没有分布式事务，就创建。当前有分布式事务，就加入
     */
    REQUIRED,

    /**
     * 当前没有分布式事务，非分布式事务运行。当前有分布式事务，就加入
     */
    SUPPORTS
}
