package com.codingapi.example.protocol.message;

import com.codingapi.txlcn.protocol.message.separate.AbsTxCreateGroupMsg;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
public class MyTxCreateGroupMsg extends AbsTxCreateGroupMsg {

    public MyTxCreateGroupMsg(String msg) {
        groupId = msg;
    }
}
