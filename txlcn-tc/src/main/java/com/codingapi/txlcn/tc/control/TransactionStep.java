package com.codingapi.txlcn.tc.control;

import com.codingapi.maven.uml.annotation.GraphRelation;
import com.codingapi.maven.uml.annotation.Model;
import com.codingapi.txlcn.tc.info.TransactionInfo;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Model(flag = "I",value = "事务步骤",color = "#FF88EE")
public interface TransactionStep {

    @GraphRelation(value = "..>",type = TransactionState.class)
    TransactionState type();

    @GraphRelation(value = "..>",type = TransactionInfo.class)
    void run(TransactionInfo transactionInfo);

}
