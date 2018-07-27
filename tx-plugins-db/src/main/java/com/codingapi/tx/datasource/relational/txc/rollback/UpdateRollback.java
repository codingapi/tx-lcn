package com.codingapi.tx.datasource.relational.txc.rollback;

import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.codingapi.tx.datasource.relational.txc.parser.CommitInfo;
import com.codingapi.tx.datasource.relational.txc.parser.TxcField;
import com.codingapi.tx.datasource.relational.txc.parser.TxcLine;
import com.codingapi.tx.datasource.relational.txc.parser.TxcTable;
import com.codingapi.tx.datasource.relational.txc.parser.UpdateParser;
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
 * @time 17/12/14.
 */

public class UpdateRollback extends AbstractRollback{
    private Logger logger = LoggerFactory.getLogger(UpdateRollback.class);


    private static UpdateRollback instance = null;

    public static UpdateRollback getInstance() {
        if (instance == null) {
            synchronized (UpdateRollback.class) {
                if (instance == null) {
                    instance = new UpdateRollback();
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
            stringBuilder.append("update ").append(tableName).append(" ").append("set ");

            for (int i = 0; i < txcFields.size(); i++) {
                if (i == txcFields.size() - 1) {
                    stringBuilder.append(txcFields.get(i).getSqlName()).append("=").append("?");
                } else {
                    stringBuilder.append(txcFields.get(i).getSqlName()).append("=").append("?").append(",");
                }
            }
            String sql = stringBuilder.append(" where ").append(txcLine.getPrimaryKey()).append("=?").toString();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (int j = 1; j <= txcFields.size(); j++) {
                preparedStatement.setObject(j, txcFields.get(j - 1).getValue());
            }

            preparedStatement.setObject(1 + txcFields.size(), txcLine.getPrimaryValue());
            preparedStatements.add(preparedStatement);
        }

        return preparedStatements;

    }

    @Override
    protected boolean canRollback(CommitInfo commitInfo, Connection connection) throws SQLException {
        String sql = commitInfo.getSql();
        SQLUpdateStatement sqlParseStatement = (SQLUpdateStatement) new MySqlStatementParser(sql).parseStatement();

        TxcTable dbValue = UpdateParser.getInstance()
                .getOriginValue(commitInfo.getWhereParams(), sqlParseStatement, connection);

        if ( commitInfo.getOriginalValue().getLine().size() == 0) {
            logger.error("未影响行数,不回滚");
            return false;
        }

        for (TxcLine txcLine : dbValue.getLine()) {
            boolean diff = DiffUtils.diff(commitInfo.getPresentValue().getLine().get(0), txcLine);
            if (!diff) {
                try {
                    logger.error("数据不一致，不支持回滚操作, before:{},after:{}",
                            DiffUtils.getObjectMapper().writeValueAsString(commitInfo.getPresentValue().getLine().get(0)),
                            DiffUtils.getObjectMapper().writeValueAsString(txcLine));
                } catch (Exception e) {
                    logger.error("error", e);
                }
                return false;
            }
        }

        return true;
    }

}
