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
package com.codingapi.txlcn.manager.support.service.impl;

import com.codingapi.txlcn.commons.exception.TxManagerException;
import com.codingapi.txlcn.commons.util.RandomUtils;
import com.codingapi.txlcn.logger.db.TxLog;
import com.codingapi.txlcn.logger.helper.TxLcnLogDbHelper;
import com.codingapi.txlcn.logger.model.*;
import com.codingapi.txlcn.manager.config.TxManagerConfig;
import com.codingapi.txlcn.manager.support.restapi.auth.DefaultTokenStorage;
import com.codingapi.txlcn.manager.support.restapi.model.*;
import com.codingapi.txlcn.manager.support.service.AdminService;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.dto.AppInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private final TxLcnLogDbHelper txLoggerHelper;

    private final RpcClient rpcClient;

    @Autowired
    public AdminServiceImpl(TxManagerConfig managerConfig,
                            DefaultTokenStorage defaultTokenStorage,
                            TxLcnLogDbHelper txLoggerHelper,
                            RpcClient rpcClient) {
        this.managerConfig = managerConfig;
        this.defaultTokenStorage = defaultTokenStorage;
        this.txLoggerHelper = txLoggerHelper;
        this.rpcClient = rpcClient;
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
        txManagerInfo.setClientCount(rpcClient.loadAllRemoteKey().size());
        txManagerInfo.setConcurrentLevel(Math.max(
                (int) (Runtime.getRuntime().availableProcessors() / (1 - 0.8)), managerConfig.getConcurrentLevel()));
        txManagerInfo.setDtxTime(managerConfig.getDtxTime());
        txManagerInfo.setHeartbeatTime(managerConfig.getHeartTime());
        txManagerInfo.setSocketHost(managerConfig.getHost());
        txManagerInfo.setSocketPort(managerConfig.getPort());
        txManagerInfo.setExUrl(managerConfig.isExUrlEnabled() ? managerConfig.getExUrl() : "disabled");
        return txManagerInfo;
    }

    @Override
    public void deleteLogs(DeleteLogsReq deleteLogsReq) throws TxManagerException {
        List<Field> list = Stream.of(new GroupId(deleteLogsReq.getGroupId()), new Tag(deleteLogsReq.getTag()),
                new StartTime(deleteLogsReq.getLTime()), new StopTime(deleteLogsReq.getRTime()))
                .filter(Field::ok).collect(Collectors.toList());
        txLoggerHelper.deleteByFields(list);
    }

    @Override
    public ListAppMods listAppMods(Integer page, Integer limit) {
        if (Objects.isNull(limit) || limit < 1) {
            limit = 10;
        }
        if (Objects.isNull(page) || page < 1) {
            page = 1;
        }
        List<ListAppMods.AppMod> appMods = new ArrayList<>(limit);
        int firIdx = (page - 1) * limit;
        List<AppInfo> apps = rpcClient.apps();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < apps.size(); i++) {
            if (firIdx > apps.size() - 1) {
                break;
            }
            if (i < firIdx) {
                continue;
            }
            AppInfo appInfo = apps.get(i);
            ListAppMods.AppMod appMod = new ListAppMods.AppMod();
            PropertyMapper.get().from(appInfo::getName).to(appMod::setModId);
            PropertyMapper.get().from(appInfo::getCreateTime).to(t -> appMod.setRegisterTime(dateFormat.format(t)));
            appMods.add(appMod);
        }
        ListAppMods listAppMods = new ListAppMods();
        listAppMods.setTotal(apps.size());
        listAppMods.setAppMods(appMods);
        return listAppMods;
    }
}
