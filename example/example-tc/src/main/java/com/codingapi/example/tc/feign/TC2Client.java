package com.codingapi.example.tc.feign;

import com.codingapi.example.tc2.vo.DemoReq;
import com.codingapi.example.tc2.vo.DemoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TC2Client {

  @Autowired
  private RestTemplate restTemplate;

  public DemoRes save(DemoReq demoReq) {
    return restTemplate.postForObject("http://127.0.0.1:8099/save", demoReq, DemoRes.class);
  }

}
