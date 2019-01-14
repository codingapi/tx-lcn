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
package com.codingapi.txlcn.client.core.txc.control;

import com.codingapi.txlcn.client.bean.DTXLocal;
import com.codingapi.txlcn.client.bean.TxTransactionInfo;
import com.codingapi.txlcn.client.core.txc.resource.def.bean.RollbackInfo;
import com.codingapi.txlcn.client.support.TXLCNTransactionControl;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component("control_txc_default")
public class TxcDefaultTransaction implements TXLCNTransactionControl {

    @Override
    public void preBusinessCode(TxTransactionInfo info) {
        // 准备回滚信息容器
        DTXLocal.cur().setAttachment(new RollbackInfo());
        // TXC 类型事务需要代理资源
        DTXLocal.makeProxy();
    }
}
