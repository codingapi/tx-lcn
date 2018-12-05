package com.example.demo.controller;


import com.alibaba.druid.support.json.JSONUtils;
import com.codingapi.tx.compensate.model.CompensateInfo;
import com.example.demo.entity.Test;
import com.example.demo.service.DemoService;
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
    public String notifyResult(String jsonStr)
    {
        System.out.println("通知地址...");
        CompensateInfo compensateInfo = (CompensateInfo)JSONUtils.parse(jsonStr);
        System.out.println(compensateInfo);
        return null;
    }
}
