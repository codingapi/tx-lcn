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
package com.codingapi.txlcn.tc.core.propagation;

import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.core.DTXPropagationState;
import com.codingapi.txlcn.tc.core.TxTransactionInfo;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.common.exception.TransactionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Description: 事务传播逻辑处理
 * Date: 2018/12/5
 *
 * @author ujued
 */
@Slf4j
@Component
public class DefaultDTXPropagationResolver implements DTXPropagationResolver {

    @Override
    public DTXPropagationState resolvePropagationState(TxTransactionInfo txTransactionInfo) throws TransactionException {

        // 本地已在DTX，根据事务传播，静默加入
        if (DTXLocalContext.cur().isInGroup()) {
            log.info("SILENT_JOIN group!");
            return DTXPropagationState.SILENT_JOIN;
        }

        // 发起方之前没有事务
        if (txTransactionInfo.isTransactionStart()) {
            // 根据事务传播，对于 SUPPORTS 不参与事务
            if (DTXPropagation.SUPPORTS.equals(txTransactionInfo.getPropagation())) {
                return DTXPropagationState.NON;
            }
            // 根据事务传播，创建事务
            return DTXPropagationState.CREATE;
        }

        // 已经存在DTX，根据事务传播，加入
        return DTXPropagationState.JOIN;
    }
}
