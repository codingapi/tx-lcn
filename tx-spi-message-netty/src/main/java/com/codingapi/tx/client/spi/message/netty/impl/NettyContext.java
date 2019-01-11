package com.codingapi.tx.client.spi.message.netty.impl;

import com.codingapi.tx.client.spi.message.netty.em.NettyType;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/21
 *
 * @author codingapi
 */
public class NettyContext {

    protected static NettyType type;

    public static NettyType currentType(){
        return type;
    }

    protected static Object params;

    public static <T> T currentParam(Class<T> tClass){
        return (T)params;
    }

}
