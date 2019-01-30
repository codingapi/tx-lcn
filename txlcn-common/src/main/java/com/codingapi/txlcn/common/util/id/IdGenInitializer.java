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
package com.codingapi.txlcn.common.util.id;

import com.codingapi.txlcn.common.runner.TxLcnInitializer;
import com.codingapi.txlcn.common.runner.TxLcnRunnerOrders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * Date: 19-1-30 下午8:50
 *
 * @author ujued
 */
@Component
public class IdGenInitializer implements TxLcnInitializer {

    private final List<IdGen> idGenList;

    @Autowired
    public IdGenInitializer(List<IdGen> idGenList) {
        this.idGenList = idGenList;
    }

    @Override
    public void init() throws Exception {
        if (idGenList.size() == 0) {
            throw new IllegalStateException("at lest one" + IdGen.class + "'s implementation.");
        }
        RandomUtils.init(idGenList.get(0));
    }

    @Override
    public int order() {
        return TxLcnRunnerOrders.MAX;
    }
}
