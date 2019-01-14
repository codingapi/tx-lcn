package com.codingapi.tx.client.core.txc.resource.def.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/17
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SelectImageParams {
    private String groupId;
    private String unitId;
    private RollbackInfo rollbackInfo;
    private List<String> primaryKeys;
    private String sql;
}
