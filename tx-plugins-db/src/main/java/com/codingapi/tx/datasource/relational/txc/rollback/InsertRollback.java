package com.codingapi.tx.datasource.relational.txc.rollback;

import com.codingapi.tx.datasource.relational.txc.TxcSqlExecutor;
import com.codingapi.tx.datasource.relational.txc.parser.CommitInfo;
import com.codingapi.tx.datasource.relational.txc.parser.TxcField;
import com.codingapi.tx.datasource.relational.txc.parser.TxcLine;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jsy.
 * @title
 * @time 17/12/25.
 */
public class InsertRollback extends AbstractRollback{

    private Logger logger = LoggerFactory.getLogger(InsertRollback.class);

    private static InsertRollback instance = null;

    public static InsertRollback getInstance() {
        if (instance == null) {
            synchronized (InsertRollback.class) {
                if (instance == null) {
                    instance = new InsertRollback();
                }
            }
        }
        return instance;
    }


    @Override
    protected List<PreparedStatement> assembleRollbackSql(CommitInfo commitInfo, Connection connection)
            throws SQLException {

        TxcLine txcLine = commitInfo.getPresentValue().getLine().get(0);
        String tableName = commitInfo.getPresentValue().getTableName();
        String sql =  "delete from " + tableName + " where " + txcLine.getPrimaryKey() + "= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setObject(1, txcLine.getPrimaryValue());
        return Lists.newArrayList(preparedStatement);
    }


    private PreparedStatement assembleQuerySql(TxcLine txcLine, String tableName, Connection connection)
            throws SQLException {
        List<TxcField> txcFields = txcLine.getFields();
        //查询db数据
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select ");

        for (int j = 0; j < txcFields.size(); j++) {
            if (j != txcFields.size() - 1) {
                stringBuffer.append(txcFields.get(j).getSqlName()).append(",");
            } else {
                stringBuffer.append(txcFields.get(j).getSqlName());
            }
        }

        stringBuffer.append(" from ").append(tableName).append(" where ").append(txcLine.getPrimaryKey()).append("= ?");

        String sql = stringBuffer.toString();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, txcLine.getPrimaryValue());
        return preparedStatement;
    }



    @Override
    protected boolean canRollback(CommitInfo commitInfo, Connection connection) throws SQLException {
        List<TxcLine> txcLines = commitInfo.getPresentValue().getLine();

        if (txcLines.size() > 1) {
            logger.error("insert操作,影响行数大于1,不支持回滚");
            return false;
        }

        if ( txcLines.size() == 0) {
            logger.error("未新影响行数,不回滚");
            return false;
        }

        TxcLine txcLine = txcLines.get(0);

        // 查询db数据
        PreparedStatement preparedStatement = assembleQuerySql(txcLine, commitInfo.getPresentValue().getTableName(), connection);
        List<TxcLine> dbValue = TxcSqlExecutor.executeQuery(preparedStatement);

        boolean diff = DiffUtils.diff(txcLines, dbValue);
        if (!diff) {
            try {
                logger.error("数据不一致，不支持回滚操作, before:{},after:{}",
                        DiffUtils.getObjectMapper().writeValueAsString(txcLines),
                        DiffUtils.getObjectMapper().writeValueAsString(dbValue));
            } catch (Exception e) {
                logger.error("error", e);
            }
            return false;
        }
        return true;
    }
}
