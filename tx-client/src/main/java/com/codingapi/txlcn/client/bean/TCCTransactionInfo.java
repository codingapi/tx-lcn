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
package com.codingapi.txlcn.client.bean;

import lombok.Data;

/**
 * @author 侯存路
 */
@Data
public class TCCTransactionInfo {


    /**
     * Tcc 事务 提交/回滚 执行类
     */
    private  Class<?> executeClass;


    /**
     *  回滚方法名称
     */
    private  String cancelMethod;


    /**
     *  提交名称
     */
    private  String confirmMethod;


    /**
     *  参数
     */
    private  Object [] methodParameter;


    /**
     *  参数类型
     */
    private  Class [] methodTypeParameter;



}
