# TxManager管理手册

访问 [http://127.0.0.1:8069/admin/index.html](http://127.0.0.1:8069/admin/index.html)进入管理后台，默认密码时`codingapi`。[指定密码](setting/manager.html)

## 后台使用说明
#### 首页信息
主要是TxManager的配置信息，不做特别说明。[注](setting/manager.html)
#### 异常记录  
`事务ID`：事务组标示    

`事务单元ID`：参与事务单元标示  

`TxClient标示`：模块标示  

`异常情况`：【未知】【TxManager通知事务】【TxClient查询事务状态】【事务发起方通知事务组】, 这几种异常情况  

`异常状态`：解决和未解决。对于系统未作出补偿的异常记录，需要系统管理员 【操作】查看当场信息，做出手动补偿  

`时间`：发生时间  

`操作`：查看异常时信息  

#### 系统日志
 * 日志分类-TAG:  
 transaction: TxClient下达命令记录
 manager: TxManager执行协调记录
  * 日志内容: 
 create group: TxClient创建事务组命令到达TxManager
 start join group: TxClient 开始加入事务组
 over join group： TxClient 成功加事务组
 notify group 1： TxManager通知TxClient提交本第事务（0回滚）
 notify unit exception： TxManager通知事务单元失败（此处会记录异常信息）
 notify group over：通知事务组结束（对于个别不能通知到的单元，会记下异常记录，不影响其它通知，异常的等待补偿）