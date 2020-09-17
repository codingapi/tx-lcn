package com.codingapi.txlcn.tm.repository;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lorne
 * @date 2020/8/3
 * @description
 */
@Data
@NoArgsConstructor
public class TransactionGroup implements Serializable {

    private String groupId;

    private TransactionState state;

    private List<TransactionInfo> transactionInfoList;

    public TransactionGroup(String groupId, String uniqueKey,String moduleName,TransactionInfo.TransactionType transactionType) {
        this.groupId = groupId;
        this.state = TransactionState.JOIN;
        this.transactionInfoList = new ArrayList<>();

        this.add(uniqueKey, moduleName,transactionType);
    }


    public void add(String uniqueKey, String moduleName, TransactionInfo.TransactionType transactionType) {
        TransactionInfo transactionInfo = new TransactionInfo(uniqueKey,moduleName,transactionType);
        this.transactionInfoList.add(transactionInfo);
    }


    public List<TransactionInfo> listTransaction() {
        List<TransactionInfo> transactionInfos = new ArrayList<>();
        for(TransactionInfo transactionInfo:transactionInfoList) {
            if(transactionInfo.hasJoin()){
                transactionInfos.add(transactionInfo);
            }
        }
        return transactionInfos;
    }

    public boolean hasCommit(){
        return state.equals(TransactionState.COMMIT);
    }


}
