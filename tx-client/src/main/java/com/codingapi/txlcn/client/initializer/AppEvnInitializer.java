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
package com.codingapi.txlcn.client.initializer;

import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
import com.codingapi.txlcn.commons.util.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/18
 *
 * @author codingapi
 */
@Component
public class AppEvnInitializer implements TxLcnInitializer {


    @Autowired
    private Environment environment;

    @Override
    public void init() throws Exception {
        String name =  environment.getProperty("spring.application.name");
        String application = StringUtils.hasText(name) ? name : "application";
        String port =  environment.getProperty("server.port");
        Transactions.setApplicationId(String.format("%s:%s", application, port));
    }


}
