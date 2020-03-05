package com.codingapi.example.tc.service;

import com.codingapi.example.tc.db.domain.Demo;
import com.codingapi.example.tc.db.mapper.DemoMapper;
import com.codingapi.example.tc2.vo.DemoReq;
import com.codingapi.example.tc2.vo.DemoRes;
import java.util.Date;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class DemoService {

  private DemoMapper demoMapper;

  @Transactional
  @LcnTransaction
  public DemoRes save(DemoReq demoReq) {
    Demo demo = new Demo();
    demo.setName(demoReq.getName());
    demo.setModule(demoReq.getModule());
    demoMapper.save(demo);
    return new DemoRes(demo.getId(), new Date());
  }

}
