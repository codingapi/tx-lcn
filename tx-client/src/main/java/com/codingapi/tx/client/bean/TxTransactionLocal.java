package com.codingapi.tx.client.bean;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 分布式事务远程调用控制对象
 * Created by lorne on 2017/6/5.
 */
@Data
@Slf4j
public class TxTransactionLocal {

    private final static ThreadLocal<TxTransactionLocal> currentLocal = new InheritableThreadLocal<TxTransactionLocal>();

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
     * @return
     */
    public static TxTransactionLocal current() {
        return currentLocal.get();
    }

    /**
     * 获取或新建一个线程变量
     *
     * @return
     */
    public static TxTransactionLocal getOrNew() {
        if (currentLocal.get() == null) {
            currentLocal.set(new TxTransactionLocal());
        }
        return currentLocal.get();
    }

    /**
     * 设置代理资源
     */
    public static void makeProxy() {
        if (current() != null) {
            current().proxyTmp = current().proxy;
            current().proxy = true;
        }
    }

    /**
     * 设置不代理资源
     */
    public static void makeUnProxy() {
        if (current() != null) {
            current().proxyTmp = current().proxy;
            current().proxy = false;
        }
    }

    /**
     * 撤销到上一步的资源代理状态
     */
    public static void undoProxyStatus() {
        if (current() != null) {
            current().proxy = current().proxyTmp;
        }
    }

    /**
     * 清理线程变量
     */
    public static void makeNeverAppeared() {
        if (currentLocal.get() != null) {
            log.info("clean thread local[{}]: {}", TxTransactionLocal.class.getSimpleName(), current());
            currentLocal.set(null);
        }
    }
}
