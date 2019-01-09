# 问题排查手册

TxManager 和 TxClient 都支持日志记录到数据库（TXManager默认开启日志记录）。  
日志记录格式：  
![t_logger](/img/t_logger.png)

##  在日志开启后的问题排查

### 一、TxClient  
* 事务组会话完整性检测
```sql
select * from t_logger where t_logger.group_id = '99937859659434961';
```
整个DTX下，TxClient某个事务组内容如下完整日志日路如下：  
![tx_session](/img/tx_session.png)
* 某个微服务某次事务会话
```sql
select * from t_logger where t_logger.group_id = '99937859659434961' and app_name='spring-demo-d';
```

* 其它。根据情况按日志字段查看日志记录

### 二、TxManager
TxManager 提供后台管理服务。[详情](manageradmin.html)