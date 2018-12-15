package com.example.demo.client;

import com.example.demo.entity.Test;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by lorne on 2017/6/27.
 */
@FeignClient(value = "demo3", fallback = Demo3ClientHystric.class)
public interface Demo3Client {

    @RequestMapping(value = "/demo/save", method = RequestMethod.GET)
    int save();
}
