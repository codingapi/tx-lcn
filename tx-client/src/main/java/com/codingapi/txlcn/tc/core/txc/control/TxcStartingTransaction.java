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
package com.codingapi.txlcn.tc.core.txc.control;

import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.core.TxTransactionInfo;
import com.codingapi.txlcn.tc.core.txc.resource.def.bean.RollbackInfo;
import com.codingapi.txlcn.tc.support.template.TransactionControlTemplate;
import com.codingapi.txlcn.tc.core.DTXLocalControl;
import com.codingapi.txlcn.commons.exception.BeforeBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component("control_txc_starting")
@Slf4j
public class TxcStartingTransaction implements DTXLocalControl {

    private final TransactionControlTemplate transactionControlTemplate;

    @Autowired
    public TxcStartingTransaction(TransactionControlTemplate transactionControlTemplate) {
        this.transactionControlTemplate = transactionControlTemplate;
    }

    @Override
    public void preBusinessCode(TxTransactionInfo info) throws BeforeBusinessException {
        try {
            //创建事务组
            transactionControlTemplate.createGroup(
                    info.getGroupId(), info.getUnitId(), info.getTransactionInfo(), info.getTransactionType());
        } catch (Exception e) {
            throw new BeforeBusinessException(e);
        }
        // 准备回滚信息容器
        DTXLocalContext.cur().setAttachment(new RollbackInfo());

        // TXC 类型事务需要代理资源
        DTXLocalContext.makeProxy();

    }

    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        DTXLocalContext.cur().setSysTransactionState(0);

    }

    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) {
        // set state equ 1
        DTXLocalContext.cur().setSysTransactionState(1);
    }

    @Override
    public void postBusinessCode(TxTransactionInfo info) {
        RollbackInfo rollbackInfo = (RollbackInfo) DTXLocalContext.cur().getAttachment();
        int state = DTXLocalContext.transactionState();

        // 非成功状态。（事务导致）{#link TxcServiceImpl.lockResource}
        if (Objects.nonNull(rollbackInfo) && rollbackInfo.getStatus() < 0) {
            state = -1;
        }

        // 关闭事务组
        transactionControlTemplate.notifyGroup(info.getGroupId(), info.getUnitId(), info.getTransactionType(), state);
    }
}
