package com.codingapi.example.demod.service.impl;

import com.codingapi.example.common.db.domain.Demo;
import com.codingapi.example.demod.mapper.DDemoMapper;
import com.codingapi.example.demod.service.DemoService;
import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.commons.annotation.DTXPropagation;
import com.codingapi.tx.commons.annotation.TxcTransaction;
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
    private DDemoMapper demoMapper;

    @Value("${spring.application.name}")
    private String appName;


    @Override
    @TxcTransaction(dtxp = DTXPropagation.SUPPORTS)
    public String rpc(String value) {
        Demo demo = new Demo();
        demo.setCreateTime(new Date());
        demo.setDemoField(value);
        demo.setAppName(appName);
        demo.setGroupId(DTXLocal.cur().getGroupId());
        demo.setUnitId(DTXLocal.cur().getUnitId());
        demoMapper.save(demo);
        return "ok-d";
    }
}
