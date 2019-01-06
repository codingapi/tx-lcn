# 通讯指令手册

TxClient 与 TxManager 通讯指令说明文档

通讯指令数据包格式为json格式

例如:

请求包
```
{
	"key": "25303248287617950",
	"msg": {
		"action": "atg",
		"bytes": "ChEyNTMwMzIyNDYxMjczODg4OBIgNGQ1NTcwMjc3Yzg1OTI5OTI5YWUxNDBiNjFlNjA3ZjAaA3R4Yw==",
		"groupId": "25303224612738888"
	},
	"remoteKey": "/127.0.0.1:8070"
}

```
响应包
```
{
	"key": "25303248287617950",
	"msg": {
		"action": "rok",
		"groupId": "25303224612738888"
	},
	"remoteKey": "/127.0.0.1:59413"
}

```

结构说明:

| 字段   |      数据类型      |      说明      |
|----------|:-------------:|:-------------:|
| key |  string | 请求唯一标示 |
| remoteKey | string |请求资源方标示key |
| msg |    object   | 请求包数据体   |
| msg.action |    string   | 请求业务方法名   |
| msg.groupId |    string   | 事务组Id   |
| msg.bytes |    byte[]   |  序列化请求数据   |





1. 创建事务组指令

请求方向:TxClient->TxManager

请求msg对象参数:

| 字段   |      数据类型      |      说明      |
|----------|:-------------:|:-------------:|
| action |    string   |  cg   |
| groupId |    string   | 事务组Id   |

示例数据：

```
{"key":"253032248421004632","msg":{"action":"cg","groupId":"25303224612738888"},"remoteKey":"/127.0.0.1:8070"}
```

响应参数:

| 字段   |      数据类型      |      说明      |
|----------|:-------------:|:-------------:|
| action |    string   |  rok   |
| groupId |    string   | 事务组Id   |
示例数据：

```
{"key":"253032248421004632","msg":{"action":"rok","groupId":"25303224612738888"},"remoteKey":"/127.0.0.1:59360"}
```

2. 加入事务组指令

{"key":"25303248287617950","msg":{"action":"atg","bytes":"ChEyNTMwMzIyNDYxMjczODg4OBIgNGQ1NTcwMjc3Yzg1OTI5OTI5YWUxNDBiNjFlNjA3ZjAaA3R4Yw==","groupId":"25303224612738888"},"remoteKey":"/127.0.0.1:8070"}

{"key":"25303248287617950","msg":{"action":"rok","groupId":"25303224612738888"},"remoteKey":"/127.0.0.1:59413"}


3. 通知事务组指令

{"key":"253032592607473146","msg":{"action":"clg","bytes":"ChEyNTMwMzIyNDYxMjczODg4OBAA","groupId":"25303224612738888"},"remoteKey":"/127.0.0.1:8070"}

{"key":"253032592607473146","msg":{"action":"rok","groupId":"25303224612738888"},"remoteKey":"/127.0.0.1:59360"}


4. 通知事务单元指令

{"key":"25462490615581871","msg":{"action":"nt","bytes":"ChIyNTQ2MjMyNjA1MzE4ODc2NzISIDE3MmIyNGMxNTkzZDc4MTFiOGU5Y2ZhNzY0MmY4ZDIyGgN0Y2MgAQ==","groupId":"254623260531887672"},"remoteKey":"/127.0.0.1:59631"}

{"key":"25462490615581871","msg":{"action":"rok"},"remoteKey":"/127.0.0.1:8070"}

5. 事务单元检查事务状态

{"key":"251725517007399087","msg":{"action":"ats","bytes":"ChJzcHJpbmctZGVtby1jbGllbnQQAA==","groupId":"254623260531887672"},"remoteKey":"/127.0.0.1:8070"}

{"key":"251725517007399087","msg":{"action":"rok","bytes":"ChJzcHJpbmctZGVtby1jbGllbnQQAA==","groupId":"254623260531887672"},"remoteKey":"/127.0.0.1:8070"}


6. 心跳指令

{"key":"252356025399579923","msg":{"action":"h"}}

{"key":"252356025399579923","msg":{"action":"h"}}

7. 通知加入新的TxManager

{"msg":{"action":"nc","bytes":"CgkxMjcuMC4wLjEQiD8="},"remoteKey":"/127.0.0.1:60302"}


8. 客户端上报异常状态


{"key":"251725517007399087","msg":{"action":"wc","bytes":"ChJzcHJpbmctZGVtby1jbGllbnQQAA==","groupId":"254623260531887672"},"remoteKey":"/127.0.0.1:8070"}




9. 通知客户端获取事务切面日志

{"key":"251725517007399087","msg":{"action":"wc","bytes":"ChJzcHJpbmctZGVtby1jbGllbnQQAA==","groupId":"254623260531887672"},"remoteKey":"/127.0.0.1:8070"}


{"key":"251725517007399087","msg":{"action":"wc","bytes":"ChJzcHJpbmctZGVtby1jbGllbnQQAA==","groupId":"254623260531887672"},"remoteKey":"/127.0.0.1:8070"}



10. 客户端初始化指令


请求方向:TxClient->TxManager
请求参数:

{"key":"251725517007399087","msg":{"action":"ic","bytes":"ChJzcHJpbmctZGVtby1jbGllbnQQAA==","groupId":"init"},"remoteKey":"/127.0.0.1:8070"}

响应参数:

{"key":"251725517007399087","msg":{"action":"rok","bytes":"ChJzcHJpbmctZGVtby1jbGllbnQQsOoB","groupId":"init"},"remoteKey":"/127.0.0.1:59360"}



