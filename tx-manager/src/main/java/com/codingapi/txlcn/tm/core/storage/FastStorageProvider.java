package com.codingapi.txlcn.tm.core.storage;

/**
 * Description:
 * Date: 19-1-22 下午2:18
 *
 * @author ujued
 */
public interface FastStorageProvider {

    /**
     * TM FastStorage's implementation.
     *
     * @return fast storage's implementation
     */
    FastStorage provide();
}
