package com.codingapi.lcn.tx.demo.service;

import com.codingapi.lcn.tx.demo.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

/**
 * @author lorne
 * @date 2018/8/30
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
public class DemoServiceTest {

    @Autowired
    private DemoService demoService;


    @Test
    public void test(){
        User user = new User();
        user.setName("123");
        boolean isSave =  demoService.saveUser(user);
        Assert.isTrue(isSave,"执行异常.");
    }
}