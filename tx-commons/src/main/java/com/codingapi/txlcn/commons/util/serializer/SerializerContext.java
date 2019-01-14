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
 * @author lorne  2018/12/31
 *
 */
public class SerializerContext implements ISerializer {

    private ProtostuffSerializer protostuffSerializer;

    private SerializerContext(){
        protostuffSerializer = new ProtostuffSerializer();
    }

    private static SerializerContext context = null;

    public static SerializerContext getInstance() {
        if (context == null) {
            synchronized (SerializerContext.class) {
                if (context == null) {
                    context = new SerializerContext();
                }
            }
        }
        return context;
    }


    @Override
    public byte[] serialize(Object obj) throws SerializerException {
        return protostuffSerializer.serialize(obj);
    }

    @Override
    public <T> T deSerialize(byte[] param, Class<T> clazz) throws SerializerException {
        return protostuffSerializer.deSerialize(param,clazz);
    }
}
