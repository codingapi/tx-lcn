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
package com.codingapi.txlcn.client.bean;

/**
 * 分布式事务远程调用控制对象
 * Created by lorne on 2017/6/5.
 */
public class TxCompensateLocal {

    private final static ThreadLocal<TxCompensateLocal> currentLocal = new InheritableThreadLocal<>();

    private String groupId;

    private String type;

    private int startState;

    public int getStartState() {
        return startState;
    }

    public void setStartState(int startState) {
        this.startState = startState;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static TxCompensateLocal current() {
        return currentLocal.get();
    }

    public static void setCurrent(TxCompensateLocal current) {
        currentLocal.set(current);
    }

}
