package com.lorne.tx.db;

/**
 * create by lorne on 2017/8/22
 */
public interface ICallClose<T> {

    void close(T resource);

}
