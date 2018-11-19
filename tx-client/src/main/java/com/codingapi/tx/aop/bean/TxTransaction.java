package com.codingapi.tx.aop.bean;

import com.codingapi.tx.annotation.TxTransactionMode;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: wuzl
 * Date: 2018-10-19
 * Time: 10:11
 *
 * @author 吴志林
 */
public class TxTransaction {


    /**
     * 是否LCN事务发起方
     * @return true 是:是发起方 false 否:是参与方
     */
    private  boolean isStart;


    /**
     * 回滚异常
     * @return
     */
    private  Class<? extends Throwable>[] rollbackFor;


    /**
     * 不回滚异常
     * @return
     */
    private  Class<? extends Throwable>[] noRollbackFor;

    /**
     * 事务模式 仅在事务发起方配置有效
     * @return
     */
    private TxTransactionMode mode;

    /**
     * 标示本服务是否是只读
     * 若为true : 不会加入事务组; Connection 不会被 Wrap; 事务信息能正常传递
     * 在本服务无DB操作或仅有查询时请配置 true 将提高性能
     * 若应用都没有DB配置，此配置无意义不用设值
     */
    private boolean readOnly;


    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public Class<? extends Throwable>[] getRollbackFor() {
        return rollbackFor;
    }

    public void setRollbackFor(Class<? extends Throwable>[] rollbackFor) {
        this.rollbackFor = rollbackFor;
    }

    public Class<? extends Throwable>[] getNoRollbackFor() {
        return noRollbackFor;
    }

    public void setNoRollbackFor(Class<? extends Throwable>[] noRollbackFor) {
        this.noRollbackFor = noRollbackFor;
    }

    public TxTransactionMode getMode() {
        return mode;
    }

    public void setMode(TxTransactionMode mode) {
        this.mode = mode;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Class<? extends Throwable>[] rollbackFor() {
        if (rollbackFor!=null){
            return rollbackFor;
        }
        Class<Throwable>[] rs=new Class[0];
        return rs;
    }

    public Class<? extends Throwable>[] noRollbackFor() {
        if (noRollbackFor!=null){
            return noRollbackFor;
        }
        Class<Throwable>[] rs=new Class[0];
        return rs;
    }
}
