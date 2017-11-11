/*
 * Copyright (c) 2016.
 * kupats(sz)
 * www.kuparts.com.
 * Created By chenbin on 16-6-14 上午10:53.
 */

package com.codingapi.tx.utils.serializer;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.lorne.core.framework.exception.SerializerException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * <p>Description: .</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * Kryo序列化
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @since JDK 1.8
 */
public class KryoSerializer implements ISerializer {

    @Override
    public byte[] serialize(Object obj) throws SerializerException {
        byte[] bytes;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            //获取kryo对象
            Kryo kryo = new Kryo();
            Output output = new Output(outputStream);
            kryo.writeObject(output, obj);
            bytes = output.toBytes();
            output.flush();
        } catch (Exception ex) {
            throw new SerializerException("kryo serialize error" + ex.getMessage());
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {

            }
        }
        return bytes;
    }


    @Override
    public <T> T deSerialize(byte[] param, Class<T> clazz) throws SerializerException {
        T object;
        ByteArrayInputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(param);
            Kryo kryo = new Kryo();
            Input input = new Input(inputStream);
            object = kryo.readObject(input, clazz);
            input.close();
        } catch (Exception e) {
            throw new SerializerException("kryo deSerialize error" + e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException ignored) {

            }
        }
        return object;
    }
}
