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

import com.codingapi.txlcn.commons.exception.JoinGroupException;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/3
 *
 * @author ujued
 */

public interface GroupRelationship {

    void createGroup(String groupId);

    void joinGroup(String groupId, TransUnit transUnit) throws JoinGroupException;

    List<TransUnit> unitsOfGroup(String groupId);

    void removeGroup(String groupId);

    void setTransactionState(String groupId, int state);

    Short transactionState(String groupId);
}
