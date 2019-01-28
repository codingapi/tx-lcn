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
package com.codingapi.txlcn.spi.sleuth;

import brave.propagation.ExtraFieldPropagation;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/11/29
 *
 * @author lorne
 */
@Component
public class TracerHelper {


    /**
     * 事务组标示Id
     */
    public static final String GROUP_ID_FIELD_NAME = "groupId";

    /**
     * TxManager模块标示
     */
    public static final String TX_MANAGER_FIELD_NAME = "txManager";

    /**
     * 模块标示
     */
    public static final String TX_APP_LIST = "appList";


    public void createAppList(String appList) {
        ExtraFieldPropagation.set(TX_APP_LIST, appList);
    }

    public void createGroupId(String groupId) {
        ExtraFieldPropagation.set(GROUP_ID_FIELD_NAME, groupId);
    }

    public void createManagerKey(String managerKey) {
        ExtraFieldPropagation.set(TX_MANAGER_FIELD_NAME, managerKey);
    }

    public void createGroupId(String groupId, String managerKey) {
        ExtraFieldPropagation.set(GROUP_ID_FIELD_NAME, groupId);
        ExtraFieldPropagation.set(TX_MANAGER_FIELD_NAME, managerKey);
    }

    public String getGroupId() {
        return ExtraFieldPropagation.get(GROUP_ID_FIELD_NAME);
    }

    public String getTxManagerKey() {
        return ExtraFieldPropagation.get(TX_MANAGER_FIELD_NAME);
    }

    public String getAppList(){
        return ExtraFieldPropagation.get(TX_APP_LIST);
    }



}
