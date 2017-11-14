package com.codingapi.tx.framework.utils;

import com.lorne.core.framework.exception.SerializerException;


import com.codingapi.tx.model.TransactionInvocation;
import com.codingapi.tx.framework.utils.serializer.ISerializer;
import com.codingapi.tx.framework.utils.serializer.ProtostuffSerializer;

/**
 * create by lorne on 2017/8/3
 */
public class SerializerUtils {


    private static ISerializer serializer = new ProtostuffSerializer();


    public static byte[] serializeTransactionInvocation(TransactionInvocation invocation)   {
        try {
            return serializer.serialize(invocation);
        } catch (SerializerException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static TransactionInvocation parserTransactionInvocation(byte[] value)  {
        try {
            return serializer.deSerialize(value, TransactionInvocation.class);
        } catch (SerializerException e) {
            e.printStackTrace();
            return null;
        }
    }

}
