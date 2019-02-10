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
package com.codingapi.txlcn.tc.core.transaction.txc.resource;

import com.codingapi.txlcn.tc.support.p6spy.common.ConnectionInformation;
import com.codingapi.txlcn.tc.support.p6spy.wrapper.ConnectionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * @author lorne
 */
@Component
public class ConnectionHelper {

    private final CompoundJdbcEventListener compoundJdbcEventListener;

    @Autowired
    public ConnectionHelper(CompoundJdbcEventListener compoundJdbcEventListener) {
        this.compoundJdbcEventListener = compoundJdbcEventListener;
    }

    public Connection proxy(Connection connection){
        return ConnectionWrapper.wrap(connection,
                compoundJdbcEventListener,
                ConnectionInformation.fromConnection(connection));
    }


}
