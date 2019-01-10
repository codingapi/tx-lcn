# TXC事务模式

原理介绍：    
&nbsp;&nbsp;&nbsp;&nbsp;TXC模式命名来源于阿里云的GTS，实现原理是在执行SQL之前，先查询SQL的影响数据保存起来然后再执行业务。当需要回滚的时候就采用这些记录数据回滚事务。

模式特点:
* 该模式同样对代码的嵌入性低。
* 该模式仅限于对支持SQL方式的模块支持。
* 该模式由于每次执行SQL之前需要先查询影响数据，因此相比LCN模式消耗资源与时间要多。
* 该模式不会占用数据库的连接资源。

TXC 默认的数据库存储是用的Mysql，若需使用其他类型数据库可通过重新定义TxcSettingFactory Bean的方式来扩展
TxcSettingFactoryBean接口

```java

public interface TxcSettingFactory {

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
}


``` 

