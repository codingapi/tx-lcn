package com.codingapi.txlcn.tm.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020-9-13 23:07:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TmNodeInfo implements Serializable {

    private String tmId;

    /**
     * TM 节点的 ip 地址及端口
     */
    private String hostAndPort;

    /**
     * TM 的连接数
     */
    private int connection;
}
