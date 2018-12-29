package com.codingapi.tx.logger.db;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
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
    private DbHelper dbHelper;

    private RowProcessor processor = new BasicRowProcessor(new GenerousBeanProcessor());

    @PostConstruct
    public void init() {
        String sql = "CREATE TABLE IF NOT EXISTS `t_logger`  (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `group_id` varchar(50)  NOT NULL ,\n" +
                "\t`unit_id` varchar(50)  NOT NULL ,\n" +
                "\t`tag` varchar(50)  NOT NULL ,\n" +
                "\t`content` varchar(300)  NOT NULL ,\n" +
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

    public long total(String where) {
        return dbHelper.query("select count(*) from t_logger where " + where, new ScalarHandler<>());
    }

    public List<TxLog> findByLimit(int left, int right) {
        String sql = "select * from t_logger limit " + left + ", " + right;
        return dbHelper.query(sql, new BeanListHandler<>(TxLog.class, processor));
    }

    public long findByLimitTotal() {
        return total("1=1");
    }

}
