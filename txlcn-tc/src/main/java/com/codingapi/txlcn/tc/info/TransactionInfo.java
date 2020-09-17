package com.codingapi.txlcn.tc.info;

import com.codingapi.txlcn.tc.cache.Cache;
import com.codingapi.txlcn.tc.constant.TransactionConstant;
import com.codingapi.txlcn.tc.control.TransactionState;
import lombok.Data;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Data
public class TransactionInfo {

    /**
     * 事务组Id
     */
    private String groupId;

    /**
     * 事务类型
     */
    private String transactionType;

    /**
     * 事务状态
     */
    private TransactionState transactionState;

    /**
     * 业务执行状态
     * null  未知
     * true  成功响应
     * false 失败响应
     */
    private Boolean successReturn;

    public boolean isState(TransactionState state){
        if(transactionState==null){
            return false;
        }
        return transactionState.equals(state);
    }

    public TransactionInfo(TransactionState transactionState) {
        this.groupId = Cache.getGroupId();
        this.transactionState = transactionState;

        TransactionInfoThreadLocal.push(this);
    }


    public TransactionInfo(String groupId) {
        this.groupId = groupId;
        this.transactionState = TransactionState.JOIN;

        TransactionInfoThreadLocal.push(this);
    }

    public void init() {
        TransactionInfoThreadLocal.push(this);
    }

    public static TransactionInfo current(){
        return TransactionInfoThreadLocal.current();
    }

    public static void clear() {
        TransactionInfoThreadLocal.push(null);
    }

    public void setSuccessReturn(boolean successReturn) {
        this.successReturn = successReturn;
    }

    /**
     * 需要SQL代理的模式判断
     * LCN TXC
     * @return
     */
    public boolean hasSqlProxy() {
        if(transactionType.equals(TransactionConstant.LCN)){
            return true;
        }
        if(transactionType.equals(TransactionConstant.TXC)){
            return true;
        }
        return false;
    }


}
