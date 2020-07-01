package com.codingapi.txlcn.controller;

import com.codingapi.txlcn.service.DemoService;
import com.codingapi.example.tc2.vo.DemoReq;
import com.codingapi.example.tc2.vo.DemoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  @Autowired
  private DemoService demoService;

  @PostMapping("/save")
  public DemoRes save(@RequestBody DemoReq demoReq) {
    return demoService.save(demoReq);
  }

}
