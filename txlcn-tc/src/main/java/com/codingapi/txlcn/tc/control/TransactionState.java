package com.codingapi.txlcn.tc.control;

import com.codingapi.maven.uml.annotation.Model;
import com.codingapi.txlcn.tc.info.TransactionInfo;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Model(flag = "C",value = "事务状态",color = "#FF88EE")
public class TransactionState {

    private State state;

    public TransactionState() {
        TransactionInfo transactionInfo =  TransactionInfo.current();
        if(transactionInfo==null){
            state = State.CREATE;
        }else {
            if(transactionInfo.isState(this.getState())){
                state = State.NOTIFY;
            }
        }
    }

    public State getState() {
        return state;
    }

    public enum State{
        CREATE,JOIN,NOTIFY,NO_Transaction
    }


}
