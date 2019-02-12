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
package com.codingapi.txlcn.common.util;

import org.springframework.util.DigestUtils;

import java.util.Optional;

/**
 * Description: 事务相关工具类
 * Date: 2018/12/17
 *
 * @author ujued
 */
public class Transactions {

    public static String APPLICATION_ID_WHEN_RUNNING;

    /////////// 事务类型  //////////////////

    public static final String LCN = "lcn";

    public static final String TCC = "tcc";

    public static final String TXC = "txc";

    /////////// 常量 //////////////////////

    public static final String TE = "Transaction Error";

    public static final String TAG_TRANSACTION = "Transaction";

    public static final String TAG_TASK = "Transaction Task";


    /**
     * 分布式事务类型 Properties传递参数key
     */
    public static final String DTX_TYPE = "DTX_TYPE";

    /**
     * 分布式事务传播类型 Properties传递参数key
     */
    public static final String DTX_PROPAGATION = "DTX_PROPAGATION";

    /////////// 工具方法  ////////////////////////////////////////////

    /**
     * 方法签名生成事务单元ID
     *
     * @param methodSignature 方法签名key
     * @return md5hex val
     */
    public static String unitId(String methodSignature) {
        return DigestUtils.md5DigestAsHex((APPLICATION_ID_WHEN_RUNNING + methodSignature).getBytes());
    }

    public static void setApplicationIdWhenRunning(String applicationIdWhenRunning) {
        Transactions.APPLICATION_ID_WHEN_RUNNING = applicationIdWhenRunning;
    }

    /**
     * 获取应用标识。（OnRunning）
     *
     * @return ApplicationId
     */
    public static String getApplicationId() {
        return Optional.ofNullable(APPLICATION_ID_WHEN_RUNNING).orElse("unsuitable call this");
    }
}
