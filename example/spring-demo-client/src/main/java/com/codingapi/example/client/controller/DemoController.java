package com.codingapi.example.client.controller;

import com.codingapi.example.client.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Date: 2018/12/25
 *
 * @author ujued
 */
@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @RequestMapping("/txlcn")
    public String execute(String value){
        return demoService.execute(value);
    }
}
