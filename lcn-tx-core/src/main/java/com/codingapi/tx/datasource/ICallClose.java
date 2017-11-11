package com.codingapi.tx.datasource;

/**
 * create by lorne on 2017/8/22
 */
public interface ICallClose<T> {

    void close(T resource);

}
