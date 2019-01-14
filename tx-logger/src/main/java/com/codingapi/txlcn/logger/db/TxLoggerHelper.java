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
package com.codingapi.txlcn.logger.db;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/26
 *
 * @author codingapi
 */
@Component
public class TxLoggerHelper {

    @Autowired
    private LogDbHelper dbHelper;

    private RowProcessor processor = new BasicRowProcessor(new GenerousBeanProcessor());

    @PostConstruct
    public void init() {
        String sql = "CREATE TABLE IF NOT EXISTS `t_logger`  (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `group_id` varchar(50)  NOT NULL ,\n" +
                "\t`unit_id` varchar(50)  NOT NULL ,\n" +
                "\t`tag` varchar(50)  NOT NULL ,\n" +
                "\t`content` varchar(1024)  NOT NULL ,\n" +
                "  `create_time` varchar(30) NOT NULL,\n" +
                "  `app_name` varchar(50) NOT NULL,\n" +
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ") ";
        dbHelper.update(sql);

    }

    public int insert(TxLog txLoggerInfo) {
        String sql = "insert into t_logger(group_id,unit_id,tag,content,create_time,app_name) values(?,?,?,?,?,?)";
        return dbHelper.update(sql, txLoggerInfo.getGroupId(), txLoggerInfo.getUnitId(), txLoggerInfo.getTag(), txLoggerInfo.getContent(), txLoggerInfo.getCreateTime(), txLoggerInfo.getAppName());
    }

    /**
     * 按筛选条件获取记录数
     *
     * @param where where条件部分
     * @param params   参数
     * @return  总共记录数
     */
    private long total(String where, Object... params) {
        return dbHelper.query("select count(*) from t_logger where " + where, new ScalarHandler<>(), params);
    }

    /**
     * 时间排序SQL
     *
     * @param timeOrder 排序方式
     * @return  orderSql
     */
    private String timeOrderSql(int timeOrder) {
        return "order by create_time " + (timeOrder == 1 ? "asc" : "desc");
    }

    /**
     * 分页获取记录
     *
     * @param left  分页开始
     * @param right 分页结束
     * @return  结果集
     */
    public List<TxLog> findByLimit(int left, int right, int timeOrder) {
        String sql = "select * from t_logger " + timeOrderSql(timeOrder) + " limit " + left + ", " + right;
        return dbHelper.query(sql, new BeanListHandler<>(TxLog.class, processor));
    }

    /**
     * 分页获取记录所有记录数
     *
     * @return  总数
     */
    public long findByLimitTotal() {
        return total("1=1");
    }

    /**
     * GroupId 和 Tag 查询记录数
     *
     * @param groupId   groupId
     * @param tag   标示
     * @return  数量
     */
    public long findByGroupAndTagTotal(String groupId, String tag) {
        return total("group_id=? and tag=?", groupId, tag);
    }

    /**
     * GroupID 和 Tag 查询
     *
     * @param left 分页左侧
     * @param right 分页右侧
     * @param groupId   groupId
     * @param tag   标签
     * @return  数据集
     */
    public List<TxLog> findByGroupAndTag(int left, int right, String groupId, String tag, int timeOrder) {
        String sql = "select * from t_logger where group_id=? and tag=? " + timeOrderSql(timeOrder) + " limit "
                + left + ", " + right;
        return dbHelper.query(sql, new BeanListHandler<>(TxLog.class, processor), groupId, tag);
    }

    /**
     * Tag 查询记录数
     *
     * @param tag  标示
     * @return  数量
     */
    public long findByTagTotal(String tag) {
        return total("tag=?", tag);
    }

    /**
     * ag 查询
     *
     * @param left  分页左侧
     * @param right 分页右侧
     * @param tag   标签
     * @return  数据集
     */
    public List<TxLog> findByTag(int left, int right, String tag, int timeOrder) {
        String sql = "select * from t_logger where tag =? " + timeOrderSql(timeOrder) + " limit " + left + ", " + right;
        return dbHelper.query(sql, new BeanListHandler<>(TxLog.class, processor), tag);
    }

    /**
     * GroupId 查询记录数
     *
     * @param groupId   GroupId
     * @return  总数
     */
    public long findByGroupIdTotal(String groupId) {
        return total("group_id=?", groupId);
    }

    /**
     * GroupId 查询
     *
     * @param left 分页左侧
     * @param right 分页右侧
     * @param groupId 标签
     * @return 数据集
     */
    public List<TxLog> findByGroupId(int left, int right, String groupId, int timeOrder) {
        String sql = "select * from t_logger where group_id=? " + timeOrderSql(timeOrder) + " limit " + left + ", " + right;
        return dbHelper.query(sql, new BeanListHandler<>(TxLog.class, processor), groupId);
    }
}
