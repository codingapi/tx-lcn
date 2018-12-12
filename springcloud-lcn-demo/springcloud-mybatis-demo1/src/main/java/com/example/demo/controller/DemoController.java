package com.example.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.framework.utils.SerializerUtils;
import com.codingapi.tx.model.TransactionInvocation;
import com.example.demo.entity.Test;
import com.example.demo.service.DemoService;
import com.lorne.core.framework.utils.encode.Base64Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by lorne on 2017/6/26.
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;


    @RequestMapping("/list")
    @ResponseBody
    public List<Test> list(){
        return demoService.list();
    }


    @RequestMapping("/save")
    @ResponseBody
    public int save(String id, String name){
        return demoService.save(id, name);
    }

    @PostMapping("notifyResult")
    public String notifyResult(@RequestBody String jsonStr)
    {
        System.out.println("通知地址..." + jsonStr);
        String data = (String) JSONObject.parseObject(jsonStr).get("json");
        data = (String) JSONObject.parseObject(data).get("data");
        byte[] serializers =  Base64Utils.decode(data);
        TransactionInvocation transactionInvocation = SerializerUtils.parserTransactionInvocation(serializers);

        System.out.println(transactionInvocation.getMethodStr());
        return null;
    }
}
