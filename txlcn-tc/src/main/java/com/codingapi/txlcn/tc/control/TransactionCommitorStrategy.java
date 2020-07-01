package com.codingapi.txlcn.tc.control;

import com.codingapi.txlcn.tc.info.TransactionInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@AllArgsConstructor
@Slf4j
public class TransactionCommitorStrategy {

    private List<Commitor> commitors;

    private Commitor getCommitor(){
        TransactionInfo transactionInfo = TransactionInfo.current();
        for(Commitor commitor:commitors){
            if(commitor.type().equals(transactionInfo.getTransactionType())){
                return commitor;
            }
        }
        return null;
    }

    public void commit(boolean state) {
        //根据不同的事务类型区分做事务提交处理.
        Commitor commitor =  getCommitor();
        log.info("commit=> groupId:{},state:{},commitor:{}",TransactionInfo.current().getGroupId(),state,commitor);
        if(commitor!=null){
            commitor.commit(state);
        }
    }
}
