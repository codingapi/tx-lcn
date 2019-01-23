package com.codingapi.txlcn.tm.core.storage;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Date: 19-1-23 下午5:41
 *
 * @author ujued
 */
@Data
public class LockValue implements Serializable {
    private String groupId;
    private int lockType;
}
