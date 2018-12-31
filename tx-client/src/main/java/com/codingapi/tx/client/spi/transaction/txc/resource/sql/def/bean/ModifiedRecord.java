package com.codingapi.tx.client.spi.transaction.txc.resource.sql.def.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 业务操作数据库所受影响的数据库记录
 * Date: 2018/12/13
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModifiedRecord {
    /**
     * 表与字段集合的映射关系
     * {@code key} 表示记录包含的表，{@code value} 表对应受影响的字段
     */
    private Map<String, FieldCluster> fieldClusters = new HashMap<>();
}
