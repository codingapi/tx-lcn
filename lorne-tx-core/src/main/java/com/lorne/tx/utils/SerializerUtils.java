package com.lorne.tx.utils;

import com.lorne.tx.compensate.model.TransactionInvocation;
import com.lorne.tx.compensate.model.TransactionRecover;
import com.lorne.tx.exception.TransactionException;
import com.lorne.tx.serializer.ObjectSerializer;
import com.lorne.tx.serializer.ProtostuffSerializer;

/**
 * create by lorne on 2017/8/3
 */
public class SerializerUtils {


    private static ObjectSerializer serializer = new ProtostuffSerializer();


    public static byte[] serializeTransactionRecover(TransactionRecover transaction)  {
        try {
            return serializer.serialize(transaction);
        } catch (TransactionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] serializeTransactionInvocation(TransactionInvocation invocation)   {
        try {
            return serializer.serialize(invocation);
        } catch (TransactionException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static TransactionRecover parserTransactionRecover(byte[] value)  {
        try {
            return serializer.deSerialize(value, TransactionRecover.class);
        } catch (TransactionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TransactionInvocation parserTransactionInvocation(byte[] value)  {
        try {
            return serializer.deSerialize(value, TransactionInvocation.class);
        } catch (TransactionException e) {
            e.printStackTrace();
            return null;
        }
    }

}
