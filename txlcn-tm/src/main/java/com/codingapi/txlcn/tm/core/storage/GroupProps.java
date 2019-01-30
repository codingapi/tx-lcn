package com.codingapi.txlcn.tm.core.storage;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Date: 19-1-30 下午4:38
 *
 * @author ujued
 */
@Data
public class GroupProps implements Serializable {
    private String groupId;
    private long createTimeMillis;
}
