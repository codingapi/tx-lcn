package com.codingapi.lcn.tx.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lorne
 * @date 2018/8/31
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInvocation implements Serializable {

    /**
     * 事务执行器
     */
    private Class targetClazz;
    /**
     * 方法
     */
    private String method;
    /**
     * 参数值
     */
    private Object[] argumentValues;

    /**
     * 参数类型
     */
    private Class[] parameterTypes;

    /**
     * 方法字符串
     */
    private String methodStr;

}
