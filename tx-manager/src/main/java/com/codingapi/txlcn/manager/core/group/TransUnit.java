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
package com.codingapi.txlcn.manager.core.group;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 2018/12/4
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransUnit {

    /**
     * 事务单元标识
     */
    private String remoteKey;

    /**
     * 事务类型
     */
    private String unitType;

    /**
     * 相关业务方法签名
     */
    private String unitId;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
