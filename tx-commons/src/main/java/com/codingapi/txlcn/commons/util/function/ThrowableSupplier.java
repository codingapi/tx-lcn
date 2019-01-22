package com.codingapi.txlcn.commons.util.function;

/**
 * Description:
 * Date: 19-1-22 下午6:54
 *
 * @author ujued
 */
public interface ThrowableSupplier<T> {

    /**
     * 获取 {@code T} 的实例
     *
     * @return T' implementation
     * @throws Throwable ex
     */
    T get() throws Throwable;
}
