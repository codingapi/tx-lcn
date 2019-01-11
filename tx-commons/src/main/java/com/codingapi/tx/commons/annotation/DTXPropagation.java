package com.codingapi.tx.commons.annotation;

/**
 * Description:
 * Date: 19-1-11 下午4:21
 *
 * @author ujued
 */
public enum DTXPropagation {
    /**
     * 当前没有分布式事务，就创建。当前有分布式事务，就加入
     */
    REQUIRED,

    /**
     * 当前没有分布式事务，非分布式事务运行。当前有分布式事务，就加入
     */
    SUPPORTS
}
