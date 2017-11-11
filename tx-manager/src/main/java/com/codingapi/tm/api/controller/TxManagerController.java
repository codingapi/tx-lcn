package com.codingapi.tm.api.controller;

import com.codingapi.tm.api.service.TxService;
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
    private TxService txService;


    @RequestMapping("/getServer")
    public TxServer getServer() {
        return txService.getServer();
    }


    @RequestMapping("/clearTransaction")
    public boolean clearTransaction(@RequestParam("groupId") String groupId,@RequestParam("taskId") String taskId,@RequestParam("isGroup") int isGroup) {
        return txService.clearTransaction(groupId,taskId,isGroup);
    }


    @RequestMapping("/getTransaction")
    public int getTransaction(@RequestParam("groupId") String groupId,@RequestParam("taskId") String taskId) {
        return txService.getTransaction(groupId,taskId);
    }

    @RequestMapping("/sendMsg")
    public String sendMsg(@RequestParam("msg") String msg,@RequestParam("model") String model) {
        return txService.sendMsg(model,msg);
    }


    @RequestMapping("/sendCompensateMsg")
    public boolean sendCompensateMsg(@RequestParam("model") String model,@RequestParam("uniqueKey") String uniqueKey,
                                     @RequestParam("groupId") String groupId,@RequestParam("className") String className,
                                     @RequestParam("time") int time,@RequestParam("data") String data,@RequestParam("method") String method) {
        return txService.sendCompensateMsg(groupId,model,uniqueKey,className,method,data,time);
    }



    @RequestMapping("/state")
    public TxState state() {
        return txService.getState();
    }
}
