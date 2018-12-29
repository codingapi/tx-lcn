package com.codingapi.tx.commons.rpc.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * Date: 2018/12/5
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinGroupParams implements Serializable {
    private String groupId;

    /**
     * 事务单元标识
     */
    private String unitId;

    /**
     * 事务单元类型
     */
    private String unitType;

    /**
     * 通讯标识
     */
    private String remoteKey;
}
