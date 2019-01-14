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
package com.codingapi.txlcn.logger.db;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Description: 事务事件
 * Company: CodingApi
 * Date: 2018/12/19
 *
 * @author codingapi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TxLog {

    /**
     * TAG
     */
    private String tag;

    /**
     * 日志内容
     */
    private String content;

    /**
     * 事务组Id
     */
    private String groupId;

    /**
     * 事务单元Id
     */
    private String unitId;

    /**
     * 模块名称
     */
    private String appName;


    /**
     * 创建时间
     */
    private String createTime;
}
