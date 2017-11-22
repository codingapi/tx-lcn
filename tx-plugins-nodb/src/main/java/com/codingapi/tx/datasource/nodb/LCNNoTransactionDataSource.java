package com.codingapi.tx.datasource.nodb;

import com.codingapi.tx.datasource.ILCNTransactionControl;
import org.springframework.stereotype.Component;


/**
 * 没有数据库-连接池对象
 * create by lorne on 2017/7/29
 */
@Component
public class LCNNoTransactionDataSource implements ILCNTransactionControl{


    @Override
    public boolean hasGroup(String group) {
        return false;
    }


    @Override
    public boolean hasTransaction() {
        return false;
    }
}
