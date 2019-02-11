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
package com.codingapi.txlcn.logger.helper;

import com.codingapi.txlcn.common.util.Maps;
import com.codingapi.txlcn.common.util.Strings;
import com.codingapi.txlcn.logger.db.LogDbHelper;
import com.codingapi.txlcn.logger.db.LogDbProperties;
import com.codingapi.txlcn.logger.db.TxLog;
import com.codingapi.txlcn.logger.exception.NotEnableLogException;
import com.codingapi.txlcn.logger.exception.TxLoggerException;
import com.codingapi.txlcn.logger.model.*;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/26
 *
 * @author codingapi
 */
public class MysqlLoggerHelper implements TxLcnLogDbHelper {

    /**
     * 当 开启enable时才能获取到.
     */
    @Autowired(required = false)
    private LogDbHelper dbHelper;

    @Autowired
    private LogDbProperties logDbProperties;

    private RowProcessor processor = new BasicRowProcessor(new GenerousBeanProcessor());

    @Override
    public void init() {
        if (logDbProperties.isEnabled()) {
            String sql = "CREATE TABLE IF NOT EXISTS `t_logger`  (\n" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                    "  `group_id` varchar(64)  NOT NULL ,\n" +
                    "  `unit_id` varchar(32)  NOT NULL ,\n" +
                    "  `tag` varchar(50)  NOT NULL ,\n" +
                    "  `content` varchar(1024)  NOT NULL ,\n" +
                    "  `create_time` varchar(30) NOT NULL,\n" +
                    "  `app_name` varchar(128) NOT NULL,\n" +
                    "  PRIMARY KEY (`id`) USING BTREE\n" +
                    ") ";
            dbHelper.update(sql);
        }

    }

    @Override
    public int insert(TxLog txLoggerInfo) {
        if (logDbProperties.isEnabled()) {
            String sql = "insert into t_logger(group_id,unit_id,tag,content,create_time,app_name) values(?,?,?,?,?,?)";
            return dbHelper.update(sql, txLoggerInfo.getGroupId(), txLoggerInfo.getUnitId(), txLoggerInfo.getTag(),
                    Strings.format(txLoggerInfo.getContent(), Maps.of("xid", txLoggerInfo.getGroupId(),
                            "uid", txLoggerInfo.getUnitId()), txLoggerInfo.getArgs()),
                    txLoggerInfo.getCreateTime(), txLoggerInfo.getAppName());
        } else {
            throw new NotEnableLogException("not enable logger");
        }
    }

    /**
     * 按筛选条件获取记录数
     *
     * @param where  where条件部分
     * @param params 参数
     * @return 总共记录数
     */
    private long total(String where, Object... params) {
        if (logDbProperties.isEnabled()) {
            return dbHelper.query("select count(*) from t_logger where " + where, new ScalarHandler<>(), params);
        } else {
            throw new NotEnableLogException("not enable logger");
        }
    }

    /**
     * 时间排序SQL
     *
     * @param timeOrder 排序方式
     * @return orderSql
     */
    private String timeOrderSql(int timeOrder) {
        return "order by create_time " + (timeOrder == 1 ? "asc" : "desc");
    }


    @Override
    public void deleteByFields(List<Field> fields) throws TxLoggerException {
        if (Objects.isNull(dbHelper)) {
            throw new TxLoggerException("系统日志被禁用");
        }
        StringBuilder sql = new StringBuilder("delete from t_logger where 1=1 and ");
        List<String> values = whereSqlAppender(sql, fields);
        dbHelper.update(sql.toString(), values.toArray(new Object[0]));
    }

    private List<String> whereSqlAppender(StringBuilder sql, List<Field> fields) {
        List<String> values = new ArrayList<>(fields.size());
        fields.forEach(field -> {
            if (field instanceof GroupId) {
                sql.append("group_id=? and ");
                values.add(((GroupId) field).getGroupId());
            } else if (field instanceof Tag) {
                sql.append("tag=? and ");
                values.add(((Tag) field).getTag());
            } else if (field instanceof StartTime) {
                sql.append("create_time > ? and ");
                values.add(((StartTime) field).getStartTime());
            } else if (field instanceof StopTime) {
                sql.append("create_time < ? and ");
                values.add(((StopTime) field).getStopTime());
            }
        });
        sql.delete(sql.length() - 4, sql.length());
        return values;
    }

    @Override
    public LogList findByLimitAndFields(int page, int limit, int timeOrder, List<Field> list) throws TxLoggerException {
        if (Objects.isNull(dbHelper)) {
            throw new TxLoggerException("系统日志被禁用");
        }
        StringBuilder countSql = new StringBuilder("select count(*) from t_logger where 1=1 and ");
        StringBuilder sql = new StringBuilder("select * from t_logger where 1=1 and ");
        List<String> values = whereSqlAppender(sql, list);
        whereSqlAppender(countSql, list);
        Object[] params = values.toArray(new Object[0]);
        long total = dbHelper.query(countSql.toString(), new ScalarHandler<>(), params);
        if (total < (page - 1) * limit) {
            page = 1;
        }
        sql.append(timeOrderSql(timeOrder)).append(" limit ").append((page - 1) * limit).append(", ").append(limit);
        List<TxLog> txLogs = dbHelper.query(sql.toString(), new BeanListHandler<>(TxLog.class, processor), params);

        LogList logList = new LogList();
        logList.setTotal(total);
        logList.setTxLogs(txLogs);
        return logList;
    }

}
