package com.codingapi.tx.framework.task;

/**
 * create by lorne on 2017/12/21
 */
public enum TaskState {

    rollback(0),commit(1),networkError(-1),networkTimeOut(-2),connectionError(-3);


    /**
     *  数据状态：
     *  1:commit
     *  0:rollback
     *  -1:network error
     *  -2:network time out
     *  -3:execute Connection error
     * @return state
     */

    private int code;

    TaskState(int code) {
        this.code = code;
    }


    public int getCode() {
        return code;
    }
}
