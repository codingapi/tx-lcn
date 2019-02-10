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
package com.codingapi.txlcn.tm.support.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.common.exception.TransactionStateException;
import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.tm.txmsg.MessageCreator;
import com.codingapi.txlcn.tm.support.db.domain.TxException;
import com.codingapi.txlcn.tm.support.db.mybatis.TxExceptionMapper;
import com.codingapi.txlcn.tm.support.restapi.vo.ExceptionInfo;
import com.codingapi.txlcn.tm.support.restapi.vo.ExceptionList;
import com.codingapi.txlcn.tm.support.service.TxExceptionService;
import com.codingapi.txlcn.tm.support.restapi.ao.WriteTxExceptionDTO;
import com.codingapi.txlcn.tm.support.txex.TxExceptionListener;
import com.codingapi.txlcn.txmsg.RpcClient;
import com.codingapi.txlcn.txmsg.dto.MessageDto;
import com.codingapi.txlcn.txmsg.exception.RpcException;
import com.codingapi.txlcn.txmsg.util.MessageUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
@Service
@Slf4j
public class TxExceptionServiceImpl implements TxExceptionService {

    private final TxExceptionMapper txExceptionMapper;

    private final RpcClient rpcClient;

    private final TxExceptionListener txExceptionListener;

    @Autowired
    public TxExceptionServiceImpl(TxExceptionMapper txExceptionMapper, RpcClient rpcClient,
                                  TxExceptionListener txExceptionListener) {
        this.txExceptionMapper = txExceptionMapper;
        this.rpcClient = rpcClient;
        this.txExceptionListener = txExceptionListener;
    }

    @Override
    public void writeTxException(WriteTxExceptionDTO writeTxExceptionReq) {
        log.info("write tx_exception. {}", writeTxExceptionReq);
        TxException txException = new TxException();
        txException.setCreateTime(new Date());
        txException.setGroupId(writeTxExceptionReq.getGroupId());
        txException.setTransactionState(writeTxExceptionReq.getTransactionState());
        txException.setUnitId(writeTxExceptionReq.getUnitId());
        txException.setRegistrar(writeTxExceptionReq.getRegistrar());
        txException.setModId(writeTxExceptionReq.getModId());
        txException.setExState((short) 0);
        txExceptionMapper.save(txException);
        txExceptionListener.onException(txException);
    }

    @Override
    public int transactionState(String groupId) {
        log.debug("transactionState > groupId: {}", groupId);
        Integer state = txExceptionMapper.getTransactionStateByGroupId(groupId);
        if (Objects.isNull(state)) {
            return -1;
        }
        return state;
    }

    @Override
    public ExceptionList exceptionList(Integer page, Integer limit, Integer exState, String keyword, Integer registrar) {
        if (Objects.isNull(page) || page <= 0) {
            page = 1;
        }
        if (Objects.isNull(limit) || limit < 1) {
            limit = 10;
        }
        Page pageInfo = PageHelper.startPage(page, limit, true);
        List<TxException> txExceptions;
        if ((Objects.nonNull(exState) && exState != -2) && (Objects.nonNull(registrar) && registrar != -2)) {
            txExceptions = txExceptionMapper.findByExStateAndRegistrar(exState, registrar);
        } else if (Objects.nonNull(exState) && exState != -2) {
            txExceptions = txExceptionMapper.findByExState(exState);
        } else if (Objects.nonNull(registrar) && registrar != -2) {
            txExceptions = txExceptionMapper.findByRegistrar(registrar);
        } else {
            txExceptions = txExceptionMapper.findAll();
        }
        List<ExceptionInfo> exceptionInfoList = new ArrayList<>(txExceptions.size());
        for (TxException txException : txExceptions) {
            ExceptionInfo exceptionInfo = new ExceptionInfo();
            BeanUtils.copyProperties(txException, exceptionInfo);

            // 如果状态为解决，决定查下模块的日志来最终判断异常状态
            if (txException.getExState() != 1) {
                try {
                    JSONObject transactionInfo = getTransactionInfo(exceptionInfo.getGroupId(), exceptionInfo.getUnitId());
                    exceptionInfo.setTransactionInfo(transactionInfo);
                } catch (TransactionStateException e) {
                    if (e.getCode() == TransactionStateException.NON_ASPECT) {
                        // 不存在异常日志，正常
                        txExceptionMapper.changeExState(txException.getId(), (short) 1);
                        exceptionInfo.setExState((short) 1);
                    }
                }
            }
            exceptionInfoList.add(exceptionInfo);
        }
        ExceptionList exceptionList = new ExceptionList();
        exceptionList.setTotal(pageInfo.getTotal());
        exceptionList.setExceptions(exceptionInfoList);
        return exceptionList;
    }

    @Override
    public JSONObject getTransactionInfo(String groupId, String unitId) throws TransactionStateException {
        TxException exception = txExceptionMapper.getByGroupAndUnitId(groupId, unitId);
        if (Objects.isNull(exception)) {
            throw new TransactionStateException("non exists aspect log", TransactionStateException.NON_ASPECT);
        }
        List<String> remoteKeys = rpcClient.remoteKeys(exception.getModId());
        if (remoteKeys.isEmpty()) {
            throw new TransactionStateException("non mod found", TransactionStateException.NON_MOD);
        }
        try {
            for (String remoteKey : remoteKeys) {
                MessageDto messageDto = rpcClient.request(remoteKey, MessageCreator.getAspectLog(groupId, unitId), 5000);
                if (MessageUtils.statusOk(messageDto)) {
                    return messageDto.loadBean(JSONObject.class);
                }
            }
            throw new TransactionStateException("non exists aspect log", TransactionStateException.NON_ASPECT);
        } catch (RpcException e) {
            throw new TransactionStateException(e, TransactionStateException.RPC_ERR);
        }
    }

    @Override
    public void deleteExceptions(List<Long> ids) throws TxManagerException {
        txExceptionMapper.deleteByIdList(ids);
    }
}
