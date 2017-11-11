package com.codingapi.tx.utils.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import com.lorne.core.framework.exception.SerializerException;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * <p>Description: .</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * Hessian 序列化
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @since JDK 1.8
 */
public class HessianSerializer implements ISerializer {


    @Override
    public byte[] serialize(Object obj) throws SerializerException {
        ByteArrayOutputStream baos;
        try {
            baos = new ByteArrayOutputStream();
            Hessian2Output hos = new Hessian2Output(baos);
            hos.writeObject(obj);
            hos.flush();
            hos.close();
        } catch (IOException ex) {
            throw new SerializerException("Hessian serialize error " + ex.getMessage());
        }
        return baos.toByteArray();
    }

    @Override
    public <T> T deSerialize(byte[] param, Class<T> clazz) throws SerializerException {
        ByteArrayInputStream bios;
        try {
            bios = new ByteArrayInputStream(param);
            Hessian2Input his = new Hessian2Input(bios);
            return (T) his.readObject();
        } catch (IOException e) {
            throw new SerializerException("Hessian deSerialize error " + e.getMessage());
        }
    }
}
