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
package com.codingapi.txlcn.client.bean;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 分布式事务远程调用控制对象
 * Created by lorne on 2017/6/5.
 */
@Data
@Slf4j
public class DTXLocal {

    private final static ThreadLocal<DTXLocal> currentLocal = new InheritableThreadLocal<DTXLocal>();

    /**
     * 事务类型
     */
    private String transactionType;

    /**
     * 事务组
     */
    private String groupId;

    /**
     * 事务单元
     */
    private String unitId;

    /**
     * 业务相关资源
     */
    private Object resource;


    ////////////////////////// volatile ///////////////////////////////

    /**
     * 本地事务互调标识
     */
    private boolean inUnit;

    /**
     * 额外的附加值
     */
    private Object attachment;

    /**
     * 业务执行结果，1 成功 0 失败
     */
    private int state;

    /**
     * 是否代理资源
     */
    private boolean proxy;


    //////// private     ///////////////////////
    /**
     * 临时值
     */
    private boolean proxyTmp;


    private boolean isProxyTmp() {
        return proxyTmp;
    }

    private void setProxyTmp(boolean proxyTmp) {
        this.proxyTmp = proxyTmp;
    }
    ///////   end      /////////////////////////


    /**
     * 获取当前线程变量
     *
     * @return 当前线程变量
     */
    public static DTXLocal cur() {
        return currentLocal.get();
    }

    /**
     * 获取或新建一个线程变量
     *
     * @return 当前线程变量
     */
    public static DTXLocal getOrNew() {
        if (currentLocal.get() == null) {
            currentLocal.set(new DTXLocal());
        }
        return currentLocal.get();
    }

    /**
     * 设置代理资源
     */
    public static void makeProxy() {
        if (currentLocal.get() != null) {
            cur().proxyTmp = cur().proxy;
            cur().proxy = true;
        }
    }

    /**
     * 设置不代理资源
     */
    public static void makeUnProxy() {
        if (currentLocal.get() != null) {
            cur().proxyTmp = cur().proxy;
            cur().proxy = false;
        }
    }

    /**
     * 撤销到上一步的资源代理状态
     */
    public static void undoProxyStatus() {
        if (currentLocal.get() != null) {
            cur().proxy = cur().proxyTmp;
        }
    }

    /**
     * 清理线程变量
     */
    public static void makeNeverAppeared() {
        if (currentLocal.get() != null) {
            log.debug("clean thread local[{}]: {}", DTXLocal.class.getSimpleName(), cur());
            currentLocal.set(null);
        }
    }
}
