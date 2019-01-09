# 问题排查手册

## 一、概述
TxManager 和 TxClient 都支持日志记录到数据库（TXManager默认开启日志记录）。  
日志记录格式：  
![t_logger](/img/t_logger.png)

>group_id: 分布式事务组标示  
>unit_id: 参与分布式事务的某个节点标示
>tag:日志分类
>content:日志内容
>app_name:日志分类（应用级）

## 二、在日志开启后的问题排查

### 1、TxClient  
* 事务组会话完整性检测
```sql
select * from t_logger where t_logger.group_id = '99937859659434961'
```
整个DTX下，TxClient某个事务组内容如下完整日志日路如下：  
![tx_session](/img/tx_session.png)
 
* 某个微服务某次事务会话
```sql
select * 
from t_logger 
where t_logger.group_id = '99937859659434961' and app_name='spring-demo-d'
```

* 其它。根据情况按日志字段查看日志记录


##### 日志解读：
 * 日志分类-应用:  
  若基于默认配置，即非单独分配日志数据库时，日志会分别存在于各业务数据库下。若集中存储日志时，则可以按此字段查找整个事务到某个模块下的信息
 * 日志分类-TAG:  
 transaction: 事务信息  
 task: 补偿任务信息  
 txc: TXC模式事务信息  
 lcn: LCN模式事务信息  
 tcc: TCC模式事务信息  
 * 日志内容:  
 pre business code: 准备业务代码  
 [start]create group：创建分布式事务组  
 do business code：执行业务代码  
 business code success： 业务代码执行成功  
 [txc]write undo log 193： 记录回滚sql  
 [join]join group： 加入事务组  
 [join,task]start delay checking task：开始补偿任务  
 [start]notify group 1：通知事务组（提交/回滚）  
 clean transaction：清理事务  
 [txc]clear undo log：清理回滚sql  
 [join,task]stop delay checking task：关闭补偿任务  
 clean transaction over：事务清理成功  
 
 
### 2、TxManager
TxManager 提供后台管理服务。[详情](manageradmin.html)