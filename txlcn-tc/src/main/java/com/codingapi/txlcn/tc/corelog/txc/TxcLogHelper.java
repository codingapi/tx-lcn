/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.tc.corelog.txc;

import com.codingapi.txlcn.tc.core.transaction.txc.analy.def.bean.UndoLogDO;
import com.codingapi.txlcn.tc.corelog.H2DbHelper;
import com.codingapi.txlcn.tc.corelog.LogHelper;
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

    /**
     * TXC撤销日志存储表准备
     */
    @Override
    public void init() {
        h2DbHelper.update("CREATE TABLE IF NOT EXISTS TXC_UNDO_LOG (" +
                "ID BIGINT NOT NULL AUTO_INCREMENT, " +
                "UNIT_ID VARCHAR(32) NOT NULL," +
                "GROUP_ID VARCHAR(64) NOT NULL," +
                "SQL_TYPE INT NOT NULL," +
                "ROLLBACK_INFO BLOB NOT NULL," +
                "CREATE_TIME CHAR(23) NOT NULL, " +
                "PRIMARY KEY(ID) )");
        log.info("Txc log table finish (H2 DATABASE)");
    }

    /**
     * 保存TXC撤销日志
     *
     * @param undoLogDO 撤销日志对象
     * @throws SQLException 数据库操作失败
     */
    public void saveUndoLog(UndoLogDO undoLogDO) throws SQLException {
        String sql = "INSERT INTO TXC_UNDO_LOG (UNIT_ID,GROUP_ID,SQL_TYPE,ROLLBACK_INFO,CREATE_TIME) VALUES(?,?,?,?,?)";
        h2DbHelper.queryRunner().update(sql, undoLogDO.getUnitId(), undoLogDO.getGroupId(), undoLogDO.getSqlType(),
                undoLogDO.getRollbackInfo(), undoLogDO.getCreateTime());
    }

    /**
     * 获取某个事务下具体UNIT的TXC撤销日志
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @return undo log list
     * @throws SQLException 数据库操作失败
     */
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

    /**
     * 删除TXC撤销日志
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @throws SQLException 数据库操作失败
     */
    public void deleteUndoLog(String groupId, String unitId) throws SQLException {
        String sql = "DELETE FROM TXC_UNDO_LOG WHERE GROUP_ID=? AND UNIT_ID=?";
        h2DbHelper.queryRunner().update(sql, groupId, unitId);
    }
}
