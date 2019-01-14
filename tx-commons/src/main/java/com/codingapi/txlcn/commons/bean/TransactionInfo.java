/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.commons.bean;

import java.io.Serializable;

/**
 *
 * create by lorne on 2017/11/11
 */
public class TransactionInfo implements Serializable{

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

    public TransactionInfo() {
    }

    public TransactionInfo(Class targetClazz, String method, String methodStr, Object[] argumentValues, Class[] parameterTypes) {
        this.targetClazz = targetClazz;
        this.method = method;
        this.methodStr = methodStr;
        this.argumentValues = argumentValues;
        this.parameterTypes = parameterTypes;
    }

    public String getMethodStr() {
        return methodStr;
    }

    public void setMethodStr(String methodStr) {
        this.methodStr = methodStr;
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
