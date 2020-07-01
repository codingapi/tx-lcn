package com.codingapi.txlcn.tc.control.commit;

import com.codingapi.txlcn.tc.TransactionConstant;
import com.codingapi.txlcn.tc.control.Commitor;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import com.codingapi.txlcn.tc.jdbc.JdbcContext;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
public class LcnCommitor implements Commitor {


    @Override
    public String type() {
        return TransactionConstant.LCN;
    }

    @Override
    public void commit(boolean state) {
        String groupId = TransactionInfo.current().getGroupId();
        Connection connection =  JdbcContext.getInstance().get(groupId);
        if(state){
            try {
                connection.commit();
            } catch (SQLException e) {
                //todo 记录补偿

            }
        }else{
            try {
                connection.rollback();
            } catch (SQLException e) {
                //不需要关心，因为数据已经回滚了。
            }
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
