package com.codingapi.lcn.tx.demo;

import com.codingapi.lcn.tx.demo.domain.User;
import com.codingapi.lcn.tx.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * @author lorne
 * @date 2018/8/30
 * @description
 */
@SpringBootApplication
public class DemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class,args);
    }

    @Autowired
    private DemoService demoService;

    @PostConstruct
    public void test(){
        User user = new User();
        user.setName("123");
        demoService.saveUser(user);
    }
}
