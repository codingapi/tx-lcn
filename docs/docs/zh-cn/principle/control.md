# 事务控制原理

&nbsp;&nbsp;&nbsp;&nbsp;TX-LCN由两大模块组成, TxClient、TxManager，TxClient作为模块的依赖框架，提供TX-LCN的标准支持，TxManager作为分布式事务的控制放。事务发起方或者参与反都由TxClient端来控制。

原理图:

![](../img/yuanli.png)

### 核心步骤

* 创建事务组  
是指在事务发起方开始执行业务代码之前先调用TxManager创建事务组对象，然后拿到事务标示GroupId的过程。

* 加入事务组  
添加事务组是指参与方在执行完业务方法以后，将该模块的事务信息通知给TxManager的操作。

* 通知事务组  
是指在发起方执行完业务代码以后，将发起方执行结果状态通知给TxManager,TxManager将根据事务最终状态和事务组的信息来通知相应的参与模块提交或回滚事务，并返回结果给事务发起方。





