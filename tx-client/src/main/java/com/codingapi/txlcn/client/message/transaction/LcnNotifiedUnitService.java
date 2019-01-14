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
package com.codingapi.txlcn.client.message.transaction;

import com.codingapi.txlcn.client.support.common.DefaultNotifiedUnitService;
import com.codingapi.txlcn.client.support.common.template.TransactionCleanTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Date: 2018/12/11
 *
 * @author ujued
 */
@Service("rpc_lcn_notify-unit")
@Slf4j
public class LcnNotifiedUnitService extends DefaultNotifiedUnitService {

    @Autowired
    public LcnNotifiedUnitService(TransactionCleanTemplate transactionCleanTemplate) {
        super(transactionCleanTemplate);
    }
}
