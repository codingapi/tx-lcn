package com.codingapi.tx.manager.service.impl;

import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.commons.util.RandomUtils;
import com.codingapi.tx.logger.db.TxLog;
import com.codingapi.tx.logger.db.TxLoggerHelper;
import com.codingapi.tx.manager.config.TxManagerConfig;
import com.codingapi.tx.manager.restapi.auth.DefaultTokenStorage;
import com.codingapi.tx.manager.restapi.vo.DTXInfo;
import com.codingapi.tx.manager.restapi.vo.TxManagerLog;
import com.codingapi.tx.manager.restapi.vo.TxLogList;
import com.codingapi.tx.manager.restapi.vo.TxManagerInfo;
import com.codingapi.tx.manager.service.AdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private TxManagerConfig managerConfig;

    @Autowired
    private DefaultTokenStorage defaultTokenStorage;

    @Autowired
    private TxLoggerHelper txLoggerHelper;

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
    public TxLogList txLogList(Integer page, Integer limit) {
        if (Objects.isNull(page) || page < 1) {
            page = 1;
        }
        if (Objects.isNull(limit) || limit < 1) {
            limit = 10;
        }
        long total = txLoggerHelper.findByLimitTotal();
        int left = (page - 1) * limit;
        List<TxLog> txLogs = txLoggerHelper.findByLimit(left, limit);
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
        return txManagerInfo;
    }
}
