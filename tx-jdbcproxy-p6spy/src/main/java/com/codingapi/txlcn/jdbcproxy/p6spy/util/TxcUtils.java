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
package com.codingapi.txlcn.jdbcproxy.p6spy.util;

/**
 * Description:
 * Date: 19-1-14 上午10:19
 *
 * @author ujued
 */
public class TxcUtils {
    public static String txcSQL(String protoSQL) {
        return protoSQL + "_lcn";
    }

    public static String protoSQL(String txcSQL) {
        return txcSQL.substring(0, txcSQL.length() - 4);
    }

    public static boolean isTxcSQL(String sql) {
        return sql.endsWith("_lcn");
    }
}
