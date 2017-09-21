package com.lorne.tx.compensate.model;

/**
 * Created by lorne on 2017/7/15.
 */
public class QueueMsg {

    /**
     * 1 :保存
     * 0 :删除
     */
    private int type;

    private String id;

    private TransactionRecover recover;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TransactionRecover getRecover() {
        return recover;
    }

    public void setRecover(TransactionRecover recover) {
        this.recover = recover;
    }
}
