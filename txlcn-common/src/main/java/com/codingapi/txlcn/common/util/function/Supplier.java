package com.codingapi.txlcn.common.util.function;

/**
 * Description:
 * Date: 19-1-22 下午6:54
 *
 * @author ujued
 */
public interface Supplier<T, E extends Throwable> {

    /**
     * 获取 {@code T} 的实例
     *
     * @return T' implementation
     * @throws E ex
     */
    T get() throws E;
}
