package com.codingapi.example.tc.controller;

import com.codingapi.example.tc2.vo.DemoReq;
import com.codingapi.example.tc2.vo.DemoRes;
import com.codingapi.example.tc.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DemoController {

  @Autowired
  private DemoService demoService;

  @PostMapping("/save")
  public DemoRes save(@RequestBody DemoReq demoReq, HttpServletRequest request) {

    return demoService.save(demoReq);
  }

}
