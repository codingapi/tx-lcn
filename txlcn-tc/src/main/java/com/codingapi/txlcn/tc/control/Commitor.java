package com.codingapi.txlcn.tc.control;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
public interface Commitor {

    String type();

    void commit(boolean state);
}
