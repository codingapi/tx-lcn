package com.codingapi.tm.api.controller;

import com.codingapi.tm.api.service.ApiTxManagerService;
import com.codingapi.tm.model.TxServer;
import com.codingapi.tm.model.TxState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lorne on 2017/7/1.
 */
@RestController
@RequestMapping("/tx/manager")
public class TxManagerController {

    @Autowired
    private ApiTxManagerService apiTxManagerService;


    @RequestMapping("/getServer")
    public TxServer getServer() {
        return apiTxManagerService.getServer();
    }


    @RequestMapping("/clearTransaction")
    public boolean clearTransaction(@RequestParam("groupId") String groupId,@RequestParam("taskId") String taskId,@RequestParam("isGroup") int isGroup) {
        return apiTxManagerService.clearTransaction(groupId,taskId,isGroup);
    }


    @RequestMapping("/getTransaction")
    public int getTransaction(@RequestParam("groupId") String groupId,@RequestParam("taskId") String taskId) {
        return apiTxManagerService.getTransaction(groupId,taskId);
    }

    @RequestMapping("/sendMsg")
    public String sendMsg(@RequestParam("msg") String msg,@RequestParam("model") String model) {
        return apiTxManagerService.sendMsg(model,msg);
    }


    @RequestMapping("/sendCompensateMsg")
    public boolean sendCompensateMsg(@RequestParam("model") String model, @RequestParam("uniqueKey") String uniqueKey,
                                     @RequestParam("groupId") String groupId, @RequestParam("className") String className,
                                     @RequestParam("time") int time, @RequestParam("data") String data,
                                     @RequestParam("method") String method, @RequestParam("address") String address) {
        return apiTxManagerService.sendCompensateMsg(groupId, model, address, uniqueKey, className, method, data, time);
    }



    @RequestMapping("/state")
    public TxState state() {
        return apiTxManagerService.getState();
    }
}
