
package com.lorne.tx.serializer;


import com.lorne.tx.exception.TransactionException;

/**
 * <p>Description: .</p>
 * 序列化接口
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @since JDK 1.8
 */
public interface ObjectSerializer {
    /**
     * 序列化对象
     *
     * @param obj 需要序更列化的对象
     * @return byte []
     * @throws TransactionException
     */
    byte[] serialize(Object obj) throws TransactionException;

    /**
     * 反序列化对象
     *
     * @param param 需要反序列化的byte []
     * @return 对象
     * @throws TransactionException
     */
    <T> T deSerialize(byte[] param, Class<T> clazz) throws TransactionException;
}
