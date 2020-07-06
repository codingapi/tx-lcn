package com.codingapi.txlcn.tc.control.commit;

import com.codingapi.txlcn.tc.TransactionConstant;
import com.codingapi.txlcn.tc.control.Commitor;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import com.codingapi.txlcn.tc.jdbc.JdbcContext;
import com.codingapi.txlcn.tc.jdbc.log.TransactionLogExecutor;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@AllArgsConstructor
public class LcnCommitor implements Commitor {

    private TransactionLogExecutor transactionLogExecutor;

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
                transactionLogExecutor.delete(connection);
                connection.commit();
            } catch (SQLException e) {
                //todo 记录补偿
            }
        }else{
            try {
                //不需要删除补偿日志,因为日志没有存储.
//                transactionLogExecutor.delete(connection);
                connection.rollback();
            } catch (SQLException e) {
                //不需要关心，即便日志没有删除，在补偿的时候也会执行回滚操作。
            }
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
