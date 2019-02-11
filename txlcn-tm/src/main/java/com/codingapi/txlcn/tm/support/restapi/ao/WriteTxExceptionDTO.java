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
package com.codingapi.txlcn.tm.support.restapi.ao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WriteTxExceptionDTO {
    private String groupId;
    private String unitId;
    private String modId;
    private Integer transactionState;
    private Short registrar;
    private String remark;

    public WriteTxExceptionDTO(String groupId, String unitId, String modId, Integer transactionState) {
        this.groupId = groupId;
        this.unitId = unitId;
        this.transactionState = transactionState;
        this.modId = modId;
    }
}
