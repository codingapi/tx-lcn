# TXC事务模式

原理介绍：    
&nbsp;&nbsp;&nbsp;&nbsp;TXC模式命名来源于阿里云的GTS，实现原理是在执行SQL之前，先查询SQL的影响数据保存起来然后再执行业务。当需要回滚的时候就采用这些记录数据回滚事务。

模式特点:
* 该模式同样对代码的嵌入性低。
* 该模式仅限于对支持SQL方式的模块支持。
* 该模式由于每次执行SQL之前需要先查询影响数据，因此相比LCN模式消耗资源与时间要多。
* 该模式不会占用数据库的连接资源。

TXC建表语句扩展

TxClient 实现 `com.codingapi.tx.client.spi.transaction.txc.resource.init.TxcSettingFactory` 配置接口，实现自定义配置。

```java
/**
 * 允许TXC事务模式
 *
 * @return
 */
default boolean enable() {
    return true;
}

/**
 * 锁表名称
 *
 * @return
 */
default String lockTableName() {
    return SqlUtils.LOCK_TABLE;
}

/**
 * 撤销SQL信息表名
 *
 * @return
 */
default String undoLogTableName() {
    return SqlUtils.UNDO_LOG_TABLE;
}

/**
 * 事务锁表创建SQL
 *
 * @return
 */
String lockTableSql();

/**
 * 撤销日志表创建SQL
 *
 * @return
 */
String undoLogTableSql();
```
默认是对MySQL数据库的实现。如下：  
```java
@Override
public String lockTableSql() {
    return "CREATE TABLE `" + lockTableName() + "`  (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `table_name` varchar(64) NULL DEFAULT NULL,\n" +
            "  `key_value` varchar(32) NULL DEFAULT NULL,\n" +
            "  `group_id` varchar(32) NULL DEFAULT NULL,\n" +
            "  `unit_id` varchar(32) NULL DEFAULT NULL,\n" +
            "  `x_lock` int(11) NULL DEFAULT NULL,\n" +
            "  `s_lock` int(11) NULL DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`) USING BTREE,\n" +
            "  UNIQUE INDEX `table_name`(`table_name`, `key_value`, `x_lock`) USING BTREE\n" +
            ")";
}

@Override
public String undoLogTableSql() {
    return "CREATE TABLE `" + undoLogTableName() + "`  (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `gmt_create` bigint(20) NULL DEFAULT NULL,\n" +
            "  `gmt_modified` bigint(20) NULL DEFAULT NULL,\n" +
            "  `group_id` varchar(32) NULL DEFAULT NULL,\n" +
            "  `unit_id` varchar(32) NULL DEFAULT NULL,\n" +
            "  `rollback_info` blob NULL DEFAULT NULL,\n" +
            "  `status` int(11) NULL DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`) USING BTREE\n" +
            ")";
}
```