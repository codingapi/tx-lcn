package com.codingapi.example.demo.service.impl;

import com.codingapi.example.common.db.domain.Demo;
import com.codingapi.example.demo.mapper.EDemoMapper;
import com.codingapi.example.demo.service.DemoService;
import com.codingapi.tx.client.bean.TxTransactionLocal;
import com.codingapi.tx.commons.annotation.TccTransaction;
import com.codingapi.tx.commons.annotation.TxTransaction;
import com.codingapi.tx.commons.util.Transactions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Date: 2018/12/25
 *
 * @author ujued
 */
@Service
@Slf4j
public class DemoServiceImpl implements DemoService {

    @Autowired
    private EDemoMapper demoMapper;

    private ConcurrentHashMap<String, Long> ids = new ConcurrentHashMap<>();

    @Value("${spring.application.name}")
    private String appName;

    @Override
    @TccTransaction(confirmMethod = "cm", cancelMethod = "cl", executeClass = DemoServiceImpl.class)
    public String rpc(String value) {
        Demo demo = new Demo();
        demo.setDemoField(value);
        demo.setCreateTime(new Date());
        demo.setAppName(appName);
        demo.setGroupId(TxTransactionLocal.current().getGroupId());
        demo.setUnitId(TxTransactionLocal.current().getUnitId());
        demoMapper.save(demo);
        ids.put(TxTransactionLocal.current().getGroupId(), demo.getId());
        return "ok-e";
    }


    public void cm(String value) {
        log.info("tcc-confirm-" + TxTransactionLocal.getOrNew().getGroupId());
        ids.remove(TxTransactionLocal.getOrNew().getGroupId());
    }

    public void cl(String value) {
        log.info("tcc-cancel-" + TxTransactionLocal.getOrNew().getGroupId());
        demoMapper.deleteById(ids.get(TxTransactionLocal.getOrNew().getGroupId()));
    }
}
