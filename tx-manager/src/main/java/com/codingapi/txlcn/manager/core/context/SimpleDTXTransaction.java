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
package com.codingapi.txlcn.manager.core.context;

/**
 * Description:
 * Date: 1/11/19
 *
 * @author ujued
 */
public class SimpleDTXTransaction implements DTXTransaction {

    private String groupId;

    public SimpleDTXTransaction(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String groupId() {
        return groupId;
    }
}
