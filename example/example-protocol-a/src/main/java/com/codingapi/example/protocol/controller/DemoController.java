package com.codingapi.example.protocol.controller;

import com.codingapi.txlcn.protocol.server.ProtocolServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@RestController
public class DemoController {

    @Autowired
    private ProtocolServer protocolServer;

    @GetMapping("/connect")
    public int connect(@RequestParam("host") String host,@RequestParam("port") int port){
        protocolServer.connectTo(host, port);
        return 1;
    }

}
