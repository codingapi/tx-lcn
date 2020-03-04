package com.codingapi.txlcn.protocol.config;

import lombok.Data;
import lombok.ToString;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@Data
@ToString
public class Config {

    private int port = 8888;

    private int maxReadIdleSeconds = 60;

    private int maxWriteIdleSeconds = 20;

}
