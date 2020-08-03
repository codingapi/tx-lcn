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

    public TransactionGroup(String groupId, String uniqueKey,String moduleName) {
        this.groupId = groupId;
        this.state = TransactionState.JOIN;
        this.transactionInfoList = new ArrayList<>();

        this.add(uniqueKey, moduleName);
    }


    public void add(String uniqueKey, String moduleName) {
        TransactionInfo transactionInfo = new TransactionInfo(uniqueKey,moduleName);
        this.transactionInfoList.add(transactionInfo);
    }


    /**
     * 事务状态
     * 创建事务消息后的状态为 JOIN
     * 其他事务加入后的状态为 JOIN
     * 通知事物状态时确定为 COMMIT或ROLLBACK
     * 询问事务信息时若数据为空也为ROLLBACK状态
     */
    public enum TransactionState{
         JOIN,COMMIT,ROLLBACK;


        public static TransactionState parser(boolean flag){
            if(flag){
                return COMMIT;
            }else{
                return ROLLBACK;
            }
        }
    }


}
