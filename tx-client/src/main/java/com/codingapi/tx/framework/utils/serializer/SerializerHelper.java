package com.codingapi.tx.framework.utils.serializer;

import com.lorne.core.framework.exception.SerializerException;

/**
 * create by lorne on 2017/12/5
 */
public class SerializerHelper<T> {

    private static ISerializer serializer = new ProtostuffSerializer();


    public  byte[] serialize(T invocation)   {
        try {
            return serializer.serialize(invocation);
        } catch (SerializerException e) {
            e.printStackTrace();
            return null;
        }
    }


    public  T parser(byte[] value,Class<T> tClass)  {
        try {
            return (T)serializer.deSerialize(value,tClass);
        } catch (SerializerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
