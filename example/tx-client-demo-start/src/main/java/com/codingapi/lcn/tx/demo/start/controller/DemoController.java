package com.codingapi.lcn.tx.demo.start.controller;

import com.codingapi.lcn.tx.demo.start.domain.User;
import com.codingapi.lcn.tx.demo.start.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lorne
 * @date 2018/8/31
 * @description
 */
@RestController
@RequestMapping("/start")
public class DemoController {


    @Autowired
    private DemoService demoService;

    @GetMapping
    public String index(){
        User user = new User();
        user.setName("index");
        demoService.saveUser(user);
        return "success";
    }


}
