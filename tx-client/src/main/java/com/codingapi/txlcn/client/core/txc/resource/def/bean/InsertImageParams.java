package com.codingapi.txlcn.client.core.txc.resource.def.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * Date: 19-1-18 下午5:38
 *
 * @author ujued
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertImageParams {
    private String groupId;
    private String unitId;
    private String rollbackSql;
    private List<Object> params;
    private RollbackInfo rollbackInfo;
}
