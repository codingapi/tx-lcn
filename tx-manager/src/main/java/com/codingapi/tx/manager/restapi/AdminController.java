package com.codingapi.tx.manager.restapi;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.commons.bean.TransactionInfo;
import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.manager.restapi.vo.ExceptionList;
import com.codingapi.tx.manager.restapi.vo.Token;
import com.codingapi.tx.manager.restapi.vo.TxLogList;
import com.codingapi.tx.manager.restapi.vo.TxManagerInfo;
import com.codingapi.tx.manager.service.AdminService;
import com.codingapi.tx.manager.service.TxExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    private final TxExceptionService compensationService;

    @Autowired
    public AdminController(AdminService adminService, TxExceptionService compensationService) {
        this.adminService = adminService;
        this.compensationService = compensationService;
    }

    @PostMapping("/login")
    public Token login(@RequestParam("password") String password) throws TxManagerException {
        return new Token(adminService.login(password));
    }

    /**
     * 获取补偿信息
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping({"/exceptions/{page}", "/exceptions", "/exceptions/{page}/{limit}"})
    public ExceptionList exceptionList(
            @RequestParam(value = "page", required = false) @PathVariable(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) @PathVariable(value = "limit", required = false) Integer limit) {
        return compensationService.exceptionList(page, limit, null, 1);
    }

    /**
     * 获取某个事务组某个节点具体补偿信息
     *
     * @param groupId
     * @param unitId
     * @return
     */
    @GetMapping("/log/transaction-info")
    public JSONObject transactionInfo(
            @RequestParam("groupId") String groupId,
            @RequestParam("unitId") String unitId) throws TxManagerException {
        return compensationService.getTransactionInfo(groupId, unitId);
    }

    /**
     * 日志信息
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping({"/logs/{page}", "/logs/{page}/{limit}", "/logs"})
    public TxLogList txLogList(
            @RequestParam(value = "page", required = false) @PathVariable(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) @PathVariable(value = "limit", required = false) Integer limit) {
        return adminService.txLogList(page, limit);
    }

    /**
     * 获取TxManager信息
     *
     * @return
     */
    @GetMapping("/tx-manager")
    public TxManagerInfo getTxManagerInfo() {
        return adminService.getTxManagerInfo();
    }
}
