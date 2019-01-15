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
package com.codingapi.txlcn.commons.util.serializer;


import com.codingapi.txlcn.commons.exception.SerializerException;

/**
 * @author lorne 2017/11/11
 */
public interface ISerializer {
    /**
     * 序列化对象
     *
     * @param obj 需要序更列化的对象
     * @return byte []  序列号结果
     * @throws SerializerException  序列化异常
     */
    byte[] serialize(Object obj) throws SerializerException;


    /**
     * 反序列化对象
     *
     * @param param 需要反序列化的byte []
     * @param clazz 反序列化成为的bean对象Class
     * @param <T>   反序列化成为的bean对象
     * @return  对象
     * @throws SerializerException  序列化异常
     */

    <T> T deSerialize(byte[] param, Class<T> clazz) throws SerializerException;
}
