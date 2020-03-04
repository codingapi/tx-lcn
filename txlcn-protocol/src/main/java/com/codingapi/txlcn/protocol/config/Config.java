package com.codingapi.txlcn.protocol.config;

import lombok.Data;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@Data
public class Config {

    private int port = 8888;

    private int maxReadIdleSeconds = 10;

}
