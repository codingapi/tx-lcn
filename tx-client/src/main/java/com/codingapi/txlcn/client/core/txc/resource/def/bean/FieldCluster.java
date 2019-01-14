package com.codingapi.txlcn.client.core.txc.resource.def.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 某表某行记录对应的字段信息，包括主键列表和普通字段列表
 * Date: 2018/12/13
 *
 * @author ujued
 */
@NoArgsConstructor
@Data
public class FieldCluster {
    /**
     * 普通字段相关信息
     */
    private List<FieldValue> fields = new ArrayList<>();

    /**
     * 主键相关信息
     */
    private List<FieldValue> primaryKeys = new ArrayList<>();
}
