package com.codingapi.example.tc.controller;

import com.codingapi.example.tc.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  @Autowired
  private DemoService demoService;

  @GetMapping("/save")
  public boolean save(@RequestParam("name") String name) {
    return demoService.save(name);
  }

}
