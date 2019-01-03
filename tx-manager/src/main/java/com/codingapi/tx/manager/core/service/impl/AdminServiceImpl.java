package com.codingapi.tx.manager.core.service.impl;

import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.commons.util.RandomUtils;
import com.codingapi.tx.logger.db.TxLog;
import com.codingapi.tx.logger.db.TxLoggerHelper;
import com.codingapi.tx.manager.config.TxManagerConfig;
import com.codingapi.tx.manager.core.service.AdminService;
import com.codingapi.tx.manager.core.restapi.auth.DefaultTokenStorage;
import com.codingapi.tx.manager.core.restapi.model.DTXInfo;
import com.codingapi.tx.manager.core.restapi.model.TxManagerLog;
import com.codingapi.tx.manager.core.restapi.model.TxLogList;
import com.codingapi.tx.manager.core.restapi.model.TxManagerInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/28
 *
 * @author ujued
 */
@Service
public class AdminServiceImpl implements AdminService {

    private final TxManagerConfig managerConfig;

    private final DefaultTokenStorage defaultTokenStorage;

    private final TxLoggerHelper txLoggerHelper;

    @Autowired
    public AdminServiceImpl(TxManagerConfig managerConfig, DefaultTokenStorage defaultTokenStorage, TxLoggerHelper txLoggerHelper) {
        this.managerConfig = managerConfig;
        this.defaultTokenStorage = defaultTokenStorage;
        this.txLoggerHelper = txLoggerHelper;
    }

    @Override
    public String login(String password) throws TxManagerException {
        if (managerConfig.getAdminKey().equals(password)) {
            String token = RandomUtils.getUUID();
            defaultTokenStorage.add(token);
            return token;
        }
        throw new TxManagerException("password error.");
    }

    @Override
    public TxLogList txLogList(Integer page, Integer limit, String groupId, String tag, Integer timeOrder) {

        // 参数保证
        if (Objects.isNull(page) || page < 1) {
            page = 1;
        }
        if (Objects.isNull(limit) || limit < 1) {
            limit = 10;
        }
        if (Objects.isNull(timeOrder) || timeOrder < 1 || timeOrder > 2) {
            timeOrder = 2;
        }

        // 区分筛选条件
        long total;
        List<TxLog> txLogs;
        if (!StringUtils.isEmpty(groupId) && !StringUtils.isEmpty(tag)) {
            total = txLoggerHelper.findByGroupAndTagTotal(groupId, tag);
            if (total < (page - 1) * limit) {
                page = 1;
            }
            txLogs = txLoggerHelper.findByGroupAndTag((page - 1) * limit, limit, groupId, tag, timeOrder);
        } else if (!StringUtils.isEmpty(tag)) {
            total = txLoggerHelper.findByTagTotal(tag);
            if (total < (page - 1) * limit) {
                page = 1;
            }
            txLogs = txLoggerHelper.findByTag((page - 1) * limit, limit, tag, timeOrder);
        } else if (!StringUtils.isEmpty(groupId)) {
            total = txLoggerHelper.findByGroupIdTotal(groupId);
            if (total < (page - 1) * limit) {
                page = 1;
            }
            txLogs = txLoggerHelper.findByGroupId((page - 1) * limit, limit, groupId, timeOrder);
        } else {
            total = txLoggerHelper.findByLimitTotal();
            if (total < (page - 1) * limit) {
                page = 1;
            }
            txLogs = txLoggerHelper.findByLimit((page - 1) * limit, limit, timeOrder);
        }

        // 组装返回数据
        List<TxManagerLog> txManagerLogs = new ArrayList<>(txLogs.size());
        for (TxLog txLog : txLogs) {
            TxManagerLog txManagerLog = new TxManagerLog();
            BeanUtils.copyProperties(txLog, txManagerLog);
            txManagerLogs.add(txManagerLog);
        }
        TxLogList txLogList = new TxLogList();
        txLogList.setTotal(total);
        txLogList.setLogs(txManagerLogs);
        return txLogList;
    }

    @Override
    public DTXInfo dtxInfo() {
        return new DTXInfo();
    }

    @Override
    public TxManagerInfo getTxManagerInfo() {
        TxManagerInfo txManagerInfo = new TxManagerInfo();
        txManagerInfo.setClientCount(1);
        txManagerInfo.setConcurrentLevel(Math.max(
                (int) (Runtime.getRuntime().availableProcessors() / (1 - 0.8)), managerConfig.getConcurrentLevel()));
        txManagerInfo.setDtxTime(managerConfig.getDtxTime());
        txManagerInfo.setHeartbeatTime(managerConfig.getHeartTime());
        txManagerInfo.setRedisState((short) 1);
        txManagerInfo.setSocketHost(managerConfig.getManagerHost());
        txManagerInfo.setSocketPort(managerConfig.getRpcPort());
        txManagerInfo.setExUrl(managerConfig.getExUrl());
        return txManagerInfo;
    }
}
