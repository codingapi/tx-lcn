package com.codingapi.tx.client.springcloud.spi.sleuth.listener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppServer {

    /**
     * app key
     * example : 127.0.0.1:8080
     */
    private String key;

    /**
     * 框架业务对象
     */
    private Object server;



}
