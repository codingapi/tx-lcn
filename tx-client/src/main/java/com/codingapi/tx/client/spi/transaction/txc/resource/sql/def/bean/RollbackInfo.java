package com.codingapi.tx.client.spi.transaction.txc.resource.sql.def.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 回滚信息
 * Date: 2018/12/13
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RollbackInfo implements Serializable {

    /**
     * 回滚Sql语句列表
     */
    private List<StatementInfo> rollbackSqlList = new ArrayList<>();

    /**
     * 回滚信息状态。-1时表示不应用回滚信息
     * 此时可能是资源被锁定时失败，并未对资源做任何操作
     */
    private transient int status;
}
