package com.codingapi.tx.manager.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.commons.util.Transactions;
import com.codingapi.tx.logger.TxLogger;
import com.codingapi.tx.manager.core.restapi.model.ExceptionInfo;
import com.codingapi.tx.manager.core.restapi.model.ExceptionList;
import com.codingapi.tx.manager.core.service.TxExceptionService;
import com.codingapi.tx.manager.core.service.WriteTxExceptionDTO;
import com.codingapi.tx.manager.db.domain.TxException;
import com.codingapi.tx.manager.db.mapper.TxExceptionMapper;
import com.codingapi.tx.manager.support.rpc.MessageCreator;
import com.codingapi.tx.spi.rpc.RpcClient;
import com.codingapi.tx.spi.rpc.dto.MessageDto;
import com.codingapi.tx.spi.rpc.exception.RpcException;
import com.codingapi.tx.spi.rpc.params.TxExceptionParams;
import com.codingapi.tx.spi.rpc.util.MessageUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private TxLogger txLogger;

    @Autowired
    public TxExceptionServiceImpl(TxExceptionMapper txExceptionMapper,
                                  RpcClient rpcClient) {
        this.txExceptionMapper = txExceptionMapper;
        this.rpcClient = rpcClient;
    }

    @Override
    public void writeTxException(WriteTxExceptionDTO writeTxExceptionReq) {
        TxException txException = new TxException();
        txException.setCreateTime(new Date());
        txException.setGroupId(writeTxExceptionReq.getGroupId());
        txException.setTransactionState(writeTxExceptionReq.getTransactionState());
        txException.setUnitId(writeTxExceptionReq.getUnitId());
        txException.setRegistrar(writeTxExceptionReq.getRegistrar());
        txException.setModId(writeTxExceptionReq.getClientAddress());
        txExceptionMapper.save(txException);
    }

    @Override
    @Transactional
    public Short transactionUnitState(String groupId, String unitId) {
        log.info("transactionUnitState > groupId: {}, unitId: {}", groupId, unitId);
        TxException txException = txExceptionMapper.getByGroupAndUnitId(groupId, unitId);
        if (Objects.isNull(txException)) {
            txException = txExceptionMapper.getByGroupId(groupId);
        }
        if (Objects.isNull(txException) || Objects.isNull(txException.getTransactionState())) {
            return -1;
        }
        if (txException.getTransactionState() == 1 && txException.getRegistrar() == TxExceptionParams.NOTIFY_GROUP_ERROR) {
            txLogger.trace(groupId, unitId, Transactions.TAG_COMPENSATION, "auto compensation");
            txExceptionMapper.changeExState(txException.getId(), (short) 1);
        }
        return txException.getTransactionState();
    }

    @Override
    public ExceptionList exceptionList(Integer page, Integer limit, String keyword, int registrar) {
        if (Objects.isNull(page) || page <= 0) {
            page = 1;
        }
        if (Objects.isNull(limit) || limit < 1) {
            limit = 10;
        }
        Page pageInfo = PageHelper.startPage(page, limit, true);
        List<TxException> txExceptions = txExceptionMapper.findAll();
        List<ExceptionInfo> exceptionInfoList = new ArrayList<>(txExceptions.size());
        for (TxException txException : txExceptions) {
            ExceptionInfo exceptionInfo = new ExceptionInfo();
            BeanUtils.copyProperties(txException, exceptionInfo);
            try {
                JSONObject transactionInfo = getTransactionInfo(exceptionInfo.getGroupId(), exceptionInfo.getUnitId());
                exceptionInfo.setTransactionInfo(transactionInfo);
            } catch (TxManagerException e) {
                txExceptionMapper.changeExState(txException.getId(), (short) 1);
                exceptionInfo.setExState((short) 1);
            }
            exceptionInfoList.add(exceptionInfo);
        }
        ExceptionList exceptionList = new ExceptionList();
        exceptionList.setTotal(pageInfo.getTotal());
        exceptionList.setExceptions(exceptionInfoList);
        return exceptionList;
    }

    @Override
    public JSONObject getTransactionInfo(String groupId, String unitId) throws TxManagerException {
        TxException exception = txExceptionMapper.getByGroupAndUnitId(groupId, unitId);
        if (Objects.isNull(exception)) {
            throw new TxManagerException("non exists aspect log");
        }
        List<String> modList = rpcClient.moduleList(exception.getModId());
        if (Objects.isNull(modList)) {
            throw new TxManagerException("non mod found");
        }
        try {
            for (String mod : modList) {
                MessageDto messageDto = rpcClient.request(mod, MessageCreator.getAspectLog(groupId, unitId));
                if (MessageUtils.statusOk(messageDto)) {
                    return messageDto.loadData(JSONObject.class);
                }
            }
            throw new TxManagerException("non exists aspect log");
        } catch (RpcException | SerializerException e) {
            throw new TxManagerException(e);
        }
    }
}
