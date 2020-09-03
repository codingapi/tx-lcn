package com.codingapi.txlcn.tm.node;

import lombok.Data;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author WhomHim
 * @description TM 的 p2p 单节点
 * @date Create in 2020/9/3 17:22
 */
@Data
public class TmNode {

    /**
     * tm 的全局唯一 Id
     */
    private String id;

    private String nodeIp;

    private String port;

    /**
     * 记录 p2p 节点顺序
     */
    private LinkedBlockingDeque linkedBlockingDeque;

    private void getNodeOrder(){

    }
}
