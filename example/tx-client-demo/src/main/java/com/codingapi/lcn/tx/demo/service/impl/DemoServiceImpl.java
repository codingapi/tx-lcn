package com.codingapi.lcn.tx.demo.service.impl;

import com.codingapi.lcn.tx.annotation.TxTransaction;
import com.codingapi.lcn.tx.demo.dao.UserMapper;
import com.codingapi.lcn.tx.demo.domain.User;
import com.codingapi.lcn.tx.demo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lorne
 * @date 2018/8/30
 * @description
 */
@Service
@Slf4j
public class DemoServiceImpl implements DemoService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @TxTransaction
    public boolean saveUser(User user) {
        log.info("user->{}",user);
        int res = userMapper.insertUser(user);
        log.info("res-insertUser->{}",res);
        return res>0;
    }
}
