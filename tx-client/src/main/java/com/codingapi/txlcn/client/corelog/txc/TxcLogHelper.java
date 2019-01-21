package com.codingapi.txlcn.client.corelog.txc;

import com.codingapi.txlcn.client.core.txc.resource.def.bean.UndoLogDO;
import com.codingapi.txlcn.client.corelog.H2DbHelper;
import com.codingapi.txlcn.client.corelog.LogHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Date: 19-1-21 上午9:31
 *
 * @author ujued
 */
@Component
@Slf4j
public class TxcLogHelper implements LogHelper {

    private final H2DbHelper h2DbHelper;

    @Autowired
    public TxcLogHelper(H2DbHelper h2DbHelper) {
        this.h2DbHelper = h2DbHelper;
    }

    @Override
    public void init() {
        h2DbHelper.update("CREATE TABLE IF NOT EXISTS TXC_UNDO_LOG (" +
                "ID BIGINT NOT NULL AUTO_INCREMENT, " +
                "UNIT_ID VARCHAR(32) NOT NULL," +
                "GROUP_ID VARCHAR(32) NOT NULL," +
                "SQL_TYPE INT NOT NULL," +
                "ROLLBACK_INFO BLOB NOT NULL," +
                "CREATE_TIME CHAR(23) NOT NULL, " +
                "PRIMARY KEY(ID) )");
        log.info("Txc log table finish");
    }

    public void saveUndoLog(UndoLogDO undoLogDO) throws SQLException {
        String sql = "INSERT INTO TXC_UNDO_LOG (UNIT_ID,GROUP_ID,SQL_TYPE,ROLLBACK_INFO,CREATE_TIME) VALUES(?,?,?,?,?)";
        h2DbHelper.queryRunner().update(sql, undoLogDO.getUnitId(), undoLogDO.getGroupId(), undoLogDO.getSqlType(),
                undoLogDO.getRollbackInfo(), undoLogDO.getCreateTime());
    }

    public List<UndoLogDO> getUndoLogByGroupAndUnitId(String groupId, String unitId) throws SQLException {
        String sql = "SELECT * FROM TXC_UNDO_LOG WHERE GROUP_ID = ? and UNIT_ID = ?";
        return h2DbHelper.queryRunner().query(sql, rs -> {
            List<UndoLogDO> undoLogDOList = new ArrayList<>();
            while (rs.next()) {
                UndoLogDO undoLogDO = new UndoLogDO();
                undoLogDO.setSqlType(rs.getInt("SQL_TYPE"));
                undoLogDO.setRollbackInfo(rs.getBytes("ROLLBACK_INFO"));
                undoLogDO.setUnitId(rs.getString("UNIT_ID"));
                undoLogDO.setGroupId("GROUP_ID");
                undoLogDO.setCreateTime(rs.getString("CREATE_TIME"));
                undoLogDOList.add(undoLogDO);
            }
            return undoLogDOList;
        }, groupId, unitId);
    }

    public void deleteUndoLog(String groupId, String unitId) throws SQLException {
        String sql = "DELETE FROM TXC_UNDO_LOG WHERE GROUP_ID=? AND UNIT_ID=?";
        h2DbHelper.queryRunner().update(sql, groupId, unitId);
    }
}
