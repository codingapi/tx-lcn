package com.codingapi.tx.client.logdb.txlog;

import com.codingapi.tx.client.logdb.LogDbHelper;
import com.codingapi.tx.client.logdb.txlog.entity.TxLog;
import com.codingapi.tx.client.transaction.txc.sql.def.bean.UndoLogDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: H2数据库操作
 * Company: CodingApi
 * Date: 2018/12/19
 *
 * @author codingapi
 */
@Slf4j
@Component
public class TxLogHelper {

    private final LogDbHelper logDbHelper;

    @Autowired
    public TxLogHelper(LogDbHelper logDbHelper) {
        this.logDbHelper = logDbHelper;
    }


    @PostConstruct
    public void init() {
        logDbHelper.update("CREATE TABLE IF NOT EXISTS TXLCN_LOG " +
                "(" +
                "ID BIGINT NOT NULL AUTO_INCREMENT, " +
                "UNIT_ID VARCHAR(32) NOT NULL," +
                "GROUP_ID VARCHAR(32) NOT NULL," +
                "METHOD_STR VARCHAR(300) NOT NULL ," +
                "BYTES BLOB NOT NULL," +
                "GROUP_ID_HASH BIGINT NOT NULL," +
                "UNIT_ID_HASH BIGINT NOT NULL," +
                "TIME BIGINT NOT NULL, " +
                "PRIMARY KEY(ID) )");

        log.info("table init TXLCN_LOG finish");

    }


    public boolean save(TxLog txLog) {
        String insertSql = "INSERT INTO TXLCN_LOG(UNIT_ID,GROUP_ID,BYTES,METHOD_STR,GROUP_ID_HASH,UNIT_ID_HASH,TIME) VALUES(?,?,?,?,?,?,?)";
        return logDbHelper.update(insertSql, txLog.getUnitId(), txLog.getGroupId(), txLog.getBytes(), txLog.getMethodStr(), txLog.getGroupId().hashCode(), txLog.getUnitId().hashCode(), txLog.getTime()) > 0;
    }

    public boolean deleteAll() {
        String sql = "DELETE FROM TXLCN_LOG";
        return logDbHelper.update(sql) > 0;
    }

    public void trancute() {
        String sql = "TRUNCATE TABLE TXLCN_LOG";
        logDbHelper.update(sql);
    }

    public boolean delete(long id) {
        String sql = "DELETE FROM TXLCN_LOG WHERE ID = ?";
        return logDbHelper.update(sql, id) > 0;
    }

    public boolean delete(long groupIdHash, long unitIdHash) {
        String sql = "DELETE FROM TXLCN_LOG WHERE GROUP_ID_HASH = ? and UNIT_ID_HASH = ?";
        return logDbHelper.update(sql, groupIdHash, unitIdHash) > 0;
    }

    public boolean delete(String groupId) {
        String sql = "DELETE FROM TXLCN_LOG WHERE GROUP_ID = ?";
        return logDbHelper.update(sql, groupId) > 0;
    }

    public List<TxLog> findAll() {
        String sql = "SELECT * FROM TXLCN_LOG";
        return logDbHelper.query(sql, new ResultSetHandler<List<TxLog>>() {
            @Override
            public List<TxLog> handle(ResultSet resultSet) throws SQLException {
                List<TxLog> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(fill(resultSet));
                }
                return list;
            }
        });
    }

    public long count() {
        String sql = "SELECT count(*) FROM TXLCN_LOG";
        return logDbHelper.query(sql, new ScalarHandler<Long>());
    }

    public TxLog getTxLog(String groupId, String unitId) {
        String sql = "SELECT * FROM TXLCN_LOG WHERE GROUP_ID = ? and UNIT_ID = ?";
        return logDbHelper.query(sql, resultSetHandler, groupId, unitId);
    }

    public TxLog getTxLog(long id) {
        String sql = "SELECT * FROM TXLCN_LOG WHERE ID = ?";
        return logDbHelper.query(sql, resultSetHandler, id);
    }

    private final ResultSetHandler<TxLog> resultSetHandler = new ResultSetHandler<TxLog>() {
        @Override
        public TxLog handle(ResultSet resultSet) throws SQLException {
            if (resultSet.next()) {
                return fill(resultSet);
            }
            return null;
        }
    };


    private TxLog fill(ResultSet resultSet) throws SQLException {
        TxLog txLog = new TxLog();
        txLog.setBytes(resultSet.getBytes("BYTES"));
        txLog.setGroupId(resultSet.getString("GROUP_ID"));
        txLog.setMethodStr(resultSet.getString("METHOD_STR"));
        txLog.setTime(resultSet.getLong("TIME"));
        txLog.setUnitId(resultSet.getString("UNIT_ID"));
        txLog.setGroupIdHash(resultSet.getLong("GROUP_ID_HASH"));
        txLog.setUnitIdHash(resultSet.getLong("UNIT_ID_HASH"));
        txLog.setId(resultSet.getLong("ID"));
        return txLog;
    }

}
