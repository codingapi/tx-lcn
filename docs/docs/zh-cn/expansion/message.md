# 通讯协议扩展

通讯协议扩展是指txclient与txmanager通讯的协议扩展。

目前TX-LCN默认采用了netty方式通讯。关于拓展也以netty方式来说明如何拓展。


## 拓展tx-spi-message

主要实现6个接口，其中下面4个是由tx-spi-message的实现方提供:

* 发起请求调用客户端 `RpcClient`

```
public abstract class RpcClient {

    @Autowired
    private RpcLoadBalance rpcLoadBalance;

    /**
     * 发送指令不需要返回数据，需要知道返回的状态
     * @param rpcCmd    指令内容
     * @return  指令状态
     * @throws RpcException
     */
   public abstract RpcResponseState send(RpcCmd rpcCmd) throws RpcException;


    /**
     * 发送指令不需要返回数据，需要知道返回的状态
     * @param remoteKey 远程标识关键字
     * @param msg    指令内容
     * @return  指令状态
     * @throws RpcException
     */
    public abstract RpcResponseState send(String remoteKey,MessageDto msg) throws RpcException;


    /**
     * 发送请求并相应
     * @param rpcCmd   指令内容
     * @return  相应指令数据
     * @throws RpcException
     */
    public abstract  MessageDto request(RpcCmd rpcCmd)throws RpcException;


    /**
     * 发送请求并相应
     * @param remoteKey   远程标识关键字
     * @param msg    指令内容
     * @return  相应指令数据
     * @throws RpcException
     */
    public abstract  MessageDto request(String remoteKey,MessageDto msg)throws RpcException;


    /**
     * 获取一个远程标识关键字
     * @return
     * @throws RpcException
     */
    public  String loadRemoteKey() throws RpcException{
        return rpcLoadBalance.getRemoteKey();
    }


    /**
     * 获取所有的远程连接对象
     * @return  远程连接对象数组.
     */
    public abstract List<String> loadAllRemoteKey();


    /**
     * 获取模块远程标识
     * @param moduleName 模块名称
     * @return 远程标识
     */
    public abstract List<String> moduleList(String moduleName);


    /**
     * 绑定模块名称
     * @param remoteKey 远程标识
     * @param appName   应用名称
     */
    public abstract void bindAppName(String remoteKey,String appName) throws RpcException;



    /**
     * 获取模块名称
     * @param remoteKey 远程标识
     * @return   应用名称
     */
    public abstract  String getAppName(String remoteKey) throws RpcException;

}
```

* 发起请求调用客户端初始化接口 `RpcClientInitializer` 

```
public interface RpcClientInitializer {


    /**
     * message client init
     * @param hosts
     */
    void init(List<TxManagerHost> hosts);

    /**
     * 建立连接
     * @param socketAddress
     */
    void connect(SocketAddress socketAddress);

}

```

* TxManager message初始化接口 `RpcServerInitializer`

```
public interface RpcServerInitializer {


    /**
     * support server init
     *
     * @param managerProperties   配置信息
     */
    void init(ManagerProperties managerProperties);

}

```

* 客户端请求TxManager的负载策略 `RpcLoadBalance`

```
public interface RpcLoadBalance {

    /**
     * 获取一个远程标识关键字
     * @return
     * @throws RpcException
     */
    String getRemoteKey()throws RpcException;


}


```

下面两个用于Tx-Manager与Tx-Client的回调业务

`RpcAnswer`接口 Tx-Manager与Tx-Client都会实现用于接受响应数据。


```
public interface RpcAnswer {

    /**
     * 业务处理
     * @param rpcCmd    message 曾业务回调函数
     *
     */
    void callback(RpcCmd rpcCmd);

}

```

`ClientInitCallBack` 接口，用于Tx-Manager下需要处理客户端与TxManager建立连接的初始化回调业务。


```
public interface ClientInitCallBack {


    /**
     * 初始化连接成功回调
     * @param remoteKey 远程调用唯一key
     */
    void connected(String remoteKey);


}


```


实现细节可借鉴 tx-spi-message-netty 模块源码

