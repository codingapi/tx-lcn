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
package com.codingapi.txlcn.tm.core.storage;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Description:
 * Date: 19-1-21 下午3:13
 *
 * @author ujued
 */
@Data
public class TransactionUnit implements Serializable {

    /**
     * 事务单元所在模块标识
     */
    private String modId;

    /**
     * 事务类型
     */
    private String unitType;

    /**
     * 相关业务方法签名
     */
    private String unitId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransactionUnit that = (TransactionUnit) o;
        return Objects.equals(unitId, that.unitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitId);
    }
}
