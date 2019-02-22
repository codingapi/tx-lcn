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
package com.codingapi.txlcn.tm.support.restapi;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.common.exception.FastStorageException;
import com.codingapi.txlcn.common.exception.TransactionStateException;
import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.tm.cluster.TMProperties;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import com.codingapi.txlcn.tm.support.restapi.vo.*;
import com.codingapi.txlcn.tm.support.service.AdminService;
import com.codingapi.txlcn.tm.support.service.TxExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/28
 *
 * @author ujued
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final TxExceptionService txExceptionService;

    @Autowired
    public AdminController(AdminService adminService, TxExceptionService txExceptionService, FastStorage fastStorage) {
        this.adminService = adminService;
        this.txExceptionService = txExceptionService;
        this.fastStorage = fastStorage;
    }

    @PostMapping("/login")
    public Token login(@RequestParam("password") String password) throws TxManagerException {
        return new Token(adminService.login(password));
    }

    /**
     * 获取补偿信息
     *
     * @param page      页码
     * @param limit     记录数
     * @param extState  extState
     * @param registrar registrar
     * @return ExceptionList
     */
    @GetMapping({"/exceptions/{page}", "/exceptions", "/exceptions/{page}/{limit}"})
    public ExceptionList exceptionList(
            @RequestParam(value = "page", required = false) @PathVariable(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) @PathVariable(value = "limit", required = false) Integer limit,
            @RequestParam(value = "extState", required = false) Integer extState,
            @RequestParam(value = "registrar", required = false) Integer registrar) {
        return txExceptionService.exceptionList(page, limit, extState, null, registrar);
    }

    /**
     * 删除异常信息
     *
     * @param deleteExceptions 异常信息标示
     * @return 操作结果
     * @throws TxManagerException TxManagerException
     */
    @PostMapping("/exceptions")
    public boolean deleteExceptions(@RequestBody DeleteExceptions deleteExceptions) throws TxManagerException {
        txExceptionService.deleteExceptions(deleteExceptions.getId());
        return true;
    }

    /**
     * 获取某个事务组某个节点具体补偿信息
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @return transaction info
     * @throws TxManagerException TxManagerException
     */
    @GetMapping("/log/transaction-info")
    public JSONObject transactionInfo(
            @RequestParam("groupId") String groupId,
            @RequestParam("unitId") String unitId) throws TxManagerException {
        try {
            return txExceptionService.getTransactionInfo(groupId, unitId);
        } catch (TransactionStateException e) {
            throw new TxManagerException(e);
        }
    }

    /**
     * 日志信息
     *
     * @param page      页码
     * @param limit     记录数
     * @param groupId   groupId
     * @param tag       tag
     * @param lTime     lTime
     * @param rTime     rtime
     * @param timeOrder timeOrder
     * @return TxLogList
     * @throws TxManagerException TxManagerException
     */
    @GetMapping({"/logs/{page}", "/logs/{page}/{limit}", "/logs"})
    public TxLogList txLogList(
            @RequestParam(value = "page", required = false) @PathVariable(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) @PathVariable(value = "limit", required = false) Integer limit,
            @RequestParam(value = "groupId", required = false) String groupId,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "ld", required = false) String lTime,
            @RequestParam(value = "rd", required = false) String rTime,
            @RequestParam(value = "timeOrder", required = false) Integer timeOrder) throws TxManagerException {
        return adminService.txLogList(page, limit, groupId, tag, lTime, rTime, timeOrder);
    }

    @GetMapping({"/app-mods/{page}", "/app-mods/{page}/{limit}", "/app-mods"})
    public ListAppMods listAppMods(
            @PathVariable(value = "page", required = false) @RequestParam(value = "page", required = false) Integer page,
            @PathVariable(value = "limit", required = false) @RequestParam(value = "limit", required = false) Integer limit) {
        return adminService.listAppMods(page, limit);
    }

    /**
     * 删除日志
     *
     * @param deleteLogsReq deleteLogsReq
     * @return bool
     * @throws TxManagerException TxManagerException
     */
    @DeleteMapping("/logs")
    public boolean deleteLogs(@RequestBody DeleteLogsReq deleteLogsReq) throws TxManagerException {
        adminService.deleteLogs(deleteLogsReq);
        return true;
    }

    /**
     * 删除异常事务信息
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @param modId   modId
     * @return result always true if non exception
     * @throws TxManagerException TxManagerException
     */
    @DeleteMapping("/transaction-info")
    public boolean deleteTransactionInfo(
            @RequestParam("groupId") String groupId,
            @RequestParam("unitId") String unitId,
            @RequestParam("modId") String modId) throws TxManagerException {
        txExceptionService.deleteTransactionInfo(groupId, unitId, modId);
        return true;
    }

    /**
     * 获取TxManager信息
     *
     * @return TxManagerInfo
     */
    @GetMapping("/tx-manager")
    public TxManagerInfo getTxManagerInfo() {
        return adminService.getTxManagerInfo();
    }

    private final FastStorage fastStorage;

    @GetMapping("/tm-cluster")
    public List<TMProperties> tmList() throws FastStorageException {
        return fastStorage.findTMProperties();
    }
}
