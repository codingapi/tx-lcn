package com.codingapi.txlcn.commons.util.serializer;

import com.codingapi.txlcn.commons.exception.SerializerException;

/**
 * @author lorne
 * @date 2018/12/31
 * @description
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
