package com.codingapi.tx.service.model;

import java.io.Serializable;

/**
 * create by lorne on 2017/11/11
 */
public class TransactionInvocation  implements Serializable{

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

    public TransactionInvocation() {
    }

    public TransactionInvocation(Class targetClazz, String method, Object[] argumentValues, Class[] parameterTypes) {
        this.targetClazz = targetClazz;
        this.method = method;
        this.argumentValues = argumentValues;
        this.parameterTypes = parameterTypes;
    }

    public Class getTargetClazz() {
        return targetClazz;
    }

    public void setTargetClazz(Class targetClazz) {
        this.targetClazz = targetClazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getArgumentValues() {
        return argumentValues;
    }

    public void setArgumentValues(Object[] argumentValues) {
        this.argumentValues = argumentValues;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
