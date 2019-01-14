/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.spi.message.netty.impl;

import com.codingapi.txlcn.spi.message.netty.em.NettyType;

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
