package com.codingapi.tx.commons.util.serializer;

import com.codingapi.tx.commons.exception.SerializerException;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author lorne 2017/11/11
 */
 class ProtostuffSerializer implements ISerializer {
    private static final SchemaCache cachedSchema = SchemaCache.getInstance();
    private static final Objenesis objenesis = new ObjenesisStd(true);

    private static <T> Schema<T> getSchema(Class<T> cls) {
        return (Schema<T>) cachedSchema.get(cls);
    }



    @Override
    public byte[] serialize(Object obj) throws SerializerException {
        Class cls = obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Schema schema = getSchema(cls);
            ProtostuffIOUtil.writeTo(outputStream, obj, schema, buffer);
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
        return outputStream.toByteArray();
    }

    @Override
    public <T> T deSerialize(byte[] param, Class<T> clazz) throws SerializerException {
        T object;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(param);
            Class cls = clazz;
            object = objenesis.newInstance((Class<T>) cls);
            Schema schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(inputStream, object, schema);
            return object;
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }
}

