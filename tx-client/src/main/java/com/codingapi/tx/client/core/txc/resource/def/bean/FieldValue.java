package com.codingapi.tx.client.core.txc.resource.def.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 某行记录中某个字段相关信息
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Data
@NoArgsConstructor
public class FieldValue {
    private String tableName;
    private String fieldName;
    private Object value;
    private Class<?> valueType;
}
