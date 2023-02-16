package com.example.demo.service.impl;

import com.codingapi.tx.annotation.TxTransaction;
import com.example.demo.client.Demo2Client;
import com.example.demo.client.Demo3Client;
import com.example.demo.dao.TestMapper;
import com.example.demo.entity.Test;
import com.example.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lorne on 2017/6/26.
 */
@Service
public class DemoServiceImpl implements DemoService {


    @Autowired
    private Demo2Client demo2Client;

    @Autowired
    private Demo3Client demo3Client;

    @Autowired
    private TestMapper testMapper;

    private Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public List<Test> list() {
        return testMapper.findAll();
    }

    @Override
    @TxTransaction(isStart = true)
    @Transactional
    public int save(String name) {

        testMapper.save("demo1");

        demo2Client.save( "demo2");

        demo3Client.save();

        return 2;
    }
}
