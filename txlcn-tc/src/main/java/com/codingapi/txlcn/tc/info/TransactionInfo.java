package com.codingapi.txlcn.tc.info;

import com.codingapi.maven.uml.annotation.GraphRelation;
import com.codingapi.maven.uml.annotation.Model;
import com.codingapi.txlcn.tc.control.TransactionState;
import lombok.Data;

import java.util.UUID;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Data
@Model(flag = "C",value = "事务信息",color = "#FF88EE")
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

    @GraphRelation(value = "..>",type = TransactionState.class)
    public boolean isState(TransactionState state){
        if(transactionState==null){
            return false;
        }
        return transactionState.equals(state);
    }

    public TransactionInfo(String transactionType, TransactionState transactionState) {
        this.groupId = UUID.randomUUID().toString();
        this.transactionType = transactionType;
        this.transactionState = transactionState;

        TransactionInfoThreadLocal.push(this);
    }

    public static TransactionInfo current(){
        return TransactionInfoThreadLocal.current();
    }

    public void clear() {
        TransactionInfoThreadLocal.push(null);
    }

    public void setSuccessReturn(boolean successReturn) {
        this.successReturn = successReturn;
    }
}
