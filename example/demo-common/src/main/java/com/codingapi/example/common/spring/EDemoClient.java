package com.codingapi.example.common.spring;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description:
 * Date: 2018/12/25
 *
 * @author ujued
 */
@FeignClient(value = "spring-demo-e")
public interface EDemoClient {

    @GetMapping("/rpc")
    String rpc(@RequestParam("value") String name);
}
