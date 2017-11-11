package com.codingapi.tx.utils.serializer;


import com.lorne.core.framework.exception.SerializerException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * <p>Description: .</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * jdk序列化
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @since JDK 1.8
 */
public class JavaSerializer implements ISerializer {

    @Override
    public byte[] serialize(Object obj) throws SerializerException {
        ByteArrayOutputStream arrayOutputStream;
        try {
            arrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput objectOutput = new ObjectOutputStream(arrayOutputStream);
            objectOutput.writeObject(obj);
            objectOutput.flush();
            objectOutput.close();
        } catch (IOException e) {
            throw new SerializerException("JAVA serialize error " + e.getMessage());
        }
        return arrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deSerialize(byte[] param, Class<T> clazz) throws SerializerException {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(param);
        try {
            ObjectInput input = new ObjectInputStream(arrayInputStream);
            return (T) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializerException("JAVA deSerialize error " + e.getMessage());
        }
    }
}
