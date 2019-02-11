### 构建镜像到本地
```
# 当前路径 tx-lcn/txlcn-tm
mvn clean package -DskipTests docker:build
```

#### 运行说明
- 可以不用构建,在有docker环境的机器上直接运行下面命令

1. 命令行传参数运行

```
docker run -p 7970:7970 -p 8070:8070 --name tm \
-e spring.datasource.url=jdbc:mysql://localhost:3306/tx_manager \
-e spring.datasource.username=root \
-e spring.datasource.password=123456 -e spring.redis.host=gj.dw.cn \
-e spring.redis.port=6379 -e spring.redis.port=6379 -e spring.redis.password=dev123456 \
-e tx-lcn.manager.admin-key=123456
-d johnnywjh/txlcn.tm:5.0.1
```
- 说明
- -p 端口映射 宿主机器端口:容器内端口
- --name : 容器别名
- -d : 放入后台运行
- -e 相当于 java -jar tm.jar 后面的参数,
- spring.datasource.url 这个配置里面如果有特殊符号 命令行不支持,建议使用第二种方式

2. 增加外部配置文件运行. 需要在宿主机器上有文件 /opt/data/lcntm/application-prod.properties

```
docker run -p 7970:7970 -p 8070:8070 --name tm \
-v /opt/data/lcntm:/opt/data/lcntm \
-e spring.profiles.active=prod \
-e spring.config.additional-location=/opt/data/lcntm/application-prod.properties  \
-d johnnywjh/txlcn.tm:5.0.1
```
- 说明
- -v : 文件挂载