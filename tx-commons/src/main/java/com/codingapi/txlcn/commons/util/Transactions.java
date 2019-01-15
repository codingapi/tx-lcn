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
package com.codingapi.txlcn.commons.util;

import org.springframework.util.DigestUtils;

/**
 * Description: 事务相关工具类
 * Date: 2018/12/17
 *
 * @author ujued
 */
public class Transactions {

    /////////// 事务类型  //////////////////

    public static final String LCN = "lcn";

    public static final String TCC = "tcc";

    public static final String TXC = "txc";

    /////////// 常量 //////////////////////

    public static final String TAG_TRANSACTION = "transaction";

    public static final String TAG_TASK = "task";

    public static final String TAG_COMPENSATION = "compensation";

    /////////// 工具方法  ////////////////////////////////////////////

    /**
     * 方法签名生成事务单元ID
     *
     * @param methodSignature  方法签名key
     * @return  md5hex val
     */
    public static String unitId(String methodSignature) {
        return DigestUtils.md5DigestAsHex(methodSignature.getBytes());
    }

    /**
     * 方法签名生成补偿ID
     *
     * @param startMethodSignature 方法签名key
     * @return md5hex val
     */
    public static String compensationId(String startMethodSignature) {
        return DigestUtils.md5DigestAsHex(startMethodSignature.getBytes());
    }
}
