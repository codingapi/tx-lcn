package com.codingapi.example.client.service;

import com.codingapi.example.common.db.domain.Demo;
import com.codingapi.example.common.spring.DDemoClient;
import com.codingapi.example.common.spring.EDemoClient;
import com.codingapi.example.client.mapper.ClientDemoMapper;
import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.commons.annotation.LcnTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Description:
 * Date: 2018/12/25
 *
 * @author ujued
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private ClientDemoMapper demoMapper;

    @Autowired
    private DDemoClient dDemoClient;

    @Autowired
    private EDemoClient eDemoClient;

    @Value("${spring.application.name}")
    private String appName;

    @Override
    @LcnTransaction
    public String execute(String value) {
        String dResp = dDemoClient.rpc(value);
        String eResp = eDemoClient.rpc(value);
        Demo demo = new Demo();
        demo.setDemoField(value);
        demo.setAppName(appName);
        demo.setCreateTime(new Date());
        demo.setGroupId(DTXLocal.cur().getGroupId());
        demo.setUnitId(DTXLocal.cur().getUnitId());
        demoMapper.save(demo);
//        int i = 1/0;
        return dResp + " > " + eResp + " > " + "ok-client";
    }


}
