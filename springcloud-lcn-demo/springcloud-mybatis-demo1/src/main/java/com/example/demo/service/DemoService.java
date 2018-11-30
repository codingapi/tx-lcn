package com.example.demo.service;

import com.example.demo.entity.Test;

import java.util.List;

/**
 * Created by lorne on 2017/6/26.
 */
public interface DemoService  {

    List<Test> list();

    int save();

}
