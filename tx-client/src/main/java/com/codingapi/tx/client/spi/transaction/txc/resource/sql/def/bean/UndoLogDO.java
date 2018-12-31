package com.codingapi.tx.client.spi.transaction.txc.resource.sql.def.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 撤销日志数据对象
 * Date: 2018/12/13
 *
 * @author ujued
 */
@NoArgsConstructor
@Data
public class UndoLogDO {
    private Long id;
    private String groupId;
    private String unitId;
    private byte[] rollbackInfo;
    private long gmtCreate = System.currentTimeMillis();
    private long gmtModified = System.currentTimeMillis();
}
