
package com.codingapi.tx.utils.serializer;


import com.lorne.core.framework.exception.SerializerException;

/**
 * <p>Description: .</p>
 * 序列化接口
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @since JDK 1.8
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
