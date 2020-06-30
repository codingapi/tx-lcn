package com.codingapi.txlcn.tc.control;

import com.codingapi.maven.uml.annotation.Model;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Model(flag = "E",value = "事务状态",color = "#FF88EE")
public enum  TransactionState {

    CREATE,JOIN,NOTIFY,NO_TRANSACTION


}
