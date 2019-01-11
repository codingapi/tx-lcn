package com.codingapi.tx.commons.util;

/**
 * Description: 分布式事务参与者功能描述
 * Date: 19-1-11 下午12:58
 *
 * @author ujued
 */
public abstract class DTXFunctions {

    /**
     * 创建分布式事务组或加入
     */
    public static final int CREATE_OR_JOIN = 1;

    /**
     * 加入分布式事务组
     */
    public static final int JOIN = 2;
}
