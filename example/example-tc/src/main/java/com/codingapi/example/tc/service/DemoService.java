package com.codingapi.example.tc.service;

import com.codingapi.example.tc.db.domain.Demo;
import com.codingapi.example.tc.db.mapper.DemoMapper;
import com.codingapi.example.tc.feign.TC2Client;
import com.codingapi.example.tc2.vo.DemoReq;
import com.codingapi.example.tc2.vo.DemoRes;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DemoService {

  @Autowired
  private DemoMapper demoMapper;

  @Autowired
  private TC2Client tc2Client;

  @Transactional
  @LcnTransaction
  public boolean save(String name) {
    DemoReq demoReq = new DemoReq();
    demoReq.setName(name);
    demoReq.setModule("tc2");
    DemoRes demoRes = tc2Client.save(demoReq);
    log.info("tc2-client=>{}", demoRes);
    Demo demo = new Demo();
    demo.setName(name);
    demo.setModule("tc");
    return demoMapper.save(demo) > 0;
  }

}
