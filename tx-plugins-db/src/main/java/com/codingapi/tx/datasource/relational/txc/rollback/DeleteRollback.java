package com.codingapi.tx.datasource.relational.txc.rollback;

import com.codingapi.tx.datasource.relational.txc.parser.CommitInfo;
import com.codingapi.tx.datasource.relational.txc.parser.TxcField;
import com.codingapi.tx.datasource.relational.txc.parser.TxcLine;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jsy.
 * @title
 * @time 17/12/26.
 */
public class DeleteRollback extends AbstractRollback {

    private Logger logger = LoggerFactory.getLogger(DeleteRollback.class);

    private static DeleteRollback instance = null;

    public static DeleteRollback getInstance() {
        if (instance == null) {
            synchronized (DeleteRollback.class) {
                if (instance == null) {
                    instance = new DeleteRollback();
                }
            }
        }
        return instance;
    }

    @Override
    protected List<PreparedStatement> assembleRollbackSql(CommitInfo commitInfo, Connection connection)
            throws SQLException {

        ArrayList<PreparedStatement> preparedStatements = Lists.newArrayList();
        String tableName = commitInfo.getOriginalValue().getTableName();

        for (TxcLine txcLine : commitInfo.getOriginalValue().getLine()) {
            List<TxcField> txcFields = txcLine.getFields();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("insert into ").append(tableName).append("(");

            for (int i = 0; i < txcFields.size(); i++) {
                if (i == txcFields.size() - 1) {
                    stringBuilder.append(txcFields.get(i).getSqlName()).append(")");
                } else {
                    stringBuilder.append(txcFields.get(i).getSqlName()).append(",");
                }
            }
            stringBuilder.append(" value ").append("(");

            for (int i = 0; i < txcFields.size(); i++) {
                if (i == txcFields.size() - 1) {
                    stringBuilder.append("?").append(")");
                } else {
                    stringBuilder.append("?").append(",");
                }
            }
            String sql = stringBuilder.toString();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (int i = 1; i <= txcFields.size(); i++) {
                preparedStatement.setObject(i, txcFields.get(i - 1).getValue());
            }
            preparedStatements.add(preparedStatement);
        }


        return preparedStatements;
    }

    @Override
    protected boolean canRollback(CommitInfo commitInfo, Connection connection) throws SQLException {


        if ( commitInfo.getOriginalValue().getLine().size() == 0) {
            logger.error("未新影响行数,不回滚");
            return false;
        }

        return true;

    }
}
