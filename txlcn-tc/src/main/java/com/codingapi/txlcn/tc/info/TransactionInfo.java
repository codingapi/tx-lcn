package com.codingapi.txlcn.tc.info;

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

    public boolean isState(TransactionState.State state){
        return transactionState.getState().equals(state);
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

}
