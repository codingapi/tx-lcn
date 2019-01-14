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

/**
 * Description:
 * Date: 19-1-9 下午6:09
 *
 * @author ujued
 */
public class TransactionUnit {

    private String unitId;

    private String unitType;

    private String messageContextId;

    public TransactionUnit(String unitId, String unitType, String messageContextId) {
        this.unitId = unitId;
        this.unitType = unitType;
        this.messageContextId = messageContextId;
    }

    public String unitId() {
        return unitId;
    }

    public String messageContextId() {
        return messageContextId;
    }

    public String unitType() {
        return unitType;
    }
}
