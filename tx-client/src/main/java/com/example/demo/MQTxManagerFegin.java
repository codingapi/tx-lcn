package com.example.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yizhishang
 */
@FeignClient(value = "tx-manager")
public interface MQTxManagerFegin {

    /**
     * 检查并清理事务数据
     * @param groupId   事务组id
     * @param waitTaskId    任务id
     * @return  事务状态
     */
    @GetMapping("/tx/manager/cleanNotifyTransactionHttp")
    String cleanNotifyTransactionHttp(@RequestParam(value = "groupId") String groupId, @RequestParam(value = "waitTaskId") String waitTaskId);

    /**
     * 记录补偿事务数据到tm
     */
    @PostMapping("/tx/manager/sendCompensateMsg")
    String sendCompensateMsg(@RequestParam("currentTime") long currentTime,@RequestParam("groupId") String groupId,
                             @RequestParam("model") String model, @RequestParam("address") String address,
                             @RequestParam("uniqueKey") String uniqueKey,
                             @RequestParam("className") String className,@RequestParam("methodStr") String methodStr,
                             @RequestParam("data") String data,@RequestParam("time") long time,
                             @RequestParam("startError") int startError);

    /**
     * 获取TM服务地址
     * @return txServer
     */
    @GetMapping("/tx/manager/getServer")
    String getServer();

}
