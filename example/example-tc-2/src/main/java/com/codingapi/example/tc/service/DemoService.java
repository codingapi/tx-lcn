package com.codingapi.example.tc.service;

import com.codingapi.example.tc.db.domain.Demo;
import com.codingapi.example.tc.db.mapper.DemoMapper;
import com.codingapi.example.tc.db.mapper.LcnSqlParseTest3Mapper;
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
  private LcnSqlParseTest3Mapper lcnSqlParseTest3Mapper;

  @Transactional
  @LcnTransaction
  public DemoRes save(DemoReq demoReq) {
    Demo demo = new Demo();
    demo.setName(demoReq.getName());
    demo.setModule(demoReq.getModule());
    demoMapper.save(demo);
//    System.out.println(1/0);
    return new DemoRes(demo.getId(), new Date());
  }
  @Transactional
  @LcnTransaction
  public int del() {
    return lcnSqlParseTest3Mapper.delete();
  }
  @Transactional
  @LcnTransaction
  public int del2() {
    return lcnSqlParseTest3Mapper.delete2();
  }
}
