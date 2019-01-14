package com.codingapi.txlcn.client.core.txc.resource.def.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: SQL语句对象
 * Date: 2018/12/14
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatementInfo {

    /**
     * 带问号占位符的SQL语句
     */
    private String sql;

    /**
     * SQL占位符处的实际参数
     */
    private Object[] params;
}
