# 事务模式扩展

TX-LCN不仅仅支持LCN TXC TCC模式，也可以由开发者自定义符合TX-LCN控制原理的请求事务模型。


## 事务模式的接口定义

1. 增加一种新的事务模式名称，不能与已有的模式重名,例如test模式。

在使用新的模式时，只需要在业务上标准类型即可。如下：

```

    @TxTransaction(type = "test")
    @Transactional
    public void test(){

    }

```


2. 实现`com.codingapi.tx.client.support.resouce.TransactionResourceExecutor`接口，处理db资源。

```
public interface TransactionResourceExecutor {

    /**
     * 获取资源连接
     *
     * @param connectionSupplier Connection提供者
     * @return
     * @throws Throwable
     */
    Connection proxyConnection(Supplier<Connection> connectionSupplier) throws Throwable;

}


```

3. 针对事务模型区分事务状态，事务状态有default 、starting、 running 三种状态。


这三张状态是通过 `com.codingapi.tx.client.support.separate.TXLCNTransactionSeparator` 识别的，用户可以自定义识别器例如LCN的识别器，bean name中的`transaction_state_resolver_`是识别器名称的前缀。


```
@Slf4j
@Component("transaction_state_resolver_lcn")
public class LCNTypeTransactionSeparator extends CustomizableTransactionSeparator {

    private final TransactionAttachmentCache transactionAttachmentCache;

    private final TracerHelper tracerHelper;

    @Autowired
    public LCNTypeTransactionSeparator(TransactionAttachmentCache transactionAttachmentCache, TracerHelper tracerHelper) {
        this.transactionAttachmentCache = transactionAttachmentCache;
        this.tracerHelper = tracerHelper;
    }

    @Override
    public TXLCNTransactionState loadTransactionState(TxTransactionInfo txTransactionInfo) {

        // 不存在GroupId时不自定义
        if (tracerHelper.getGroupId() == null) {
            return super.loadTransactionState(txTransactionInfo);
        }

        // 一个模块存在多个LCN类型的事务单元在一个事务内走DEFAULT
        Optional<TransactionUnitTypeList> sameTransUnitTypeList =
                transactionAttachmentCache.attachment(tracerHelper.getGroupId(), TransactionUnitTypeList.class);
        if (sameTransUnitTypeList.isPresent() && sameTransUnitTypeList.get().contains("lcn")) {
            log.info("Default by LCN assert !");
            return TXLCNTransactionState.DEFAULT;
        }
        return super.loadTransactionState(txTransactionInfo);
    }
}

```


4. 实现不同状态下的事务控制 实现`com.codingapi.tx.client.support.separate.TXLCNTransactionControl` 接口处理业务。

```

public interface TXLCNTransactionControl {

    /**
     * 业务代码执行前
     *
     * @param info
     */
    default void preBusinessCode(TxTransactionInfo info)throws BeforeBusinessException {

    }

    /**
     * 执行业务代码
     *
     * @param info
     * @throws Throwable
     */
    default Object doBusinessCode(TxTransactionInfo info) throws Throwable {
        return info.getJoinPoint().proceed();
    }


    /**
     * 业务代码执行失败
     *
     * @param info
     * @param throwable
     */
    default void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {

    }

    /**
     * 业务代码执行成功
     *
     * @param info
     */
    default void onBusinessCodeSuccess(TxTransactionInfo info, Object result) throws TxClientException {

    }

    /**
     * 清场
     *
     * @param info
     */
    default void postBusinessCode(TxTransactionInfo info){

    }
}

```


例如 LCN starting状态下的处理实现,bean name `control_lcn_starting`是标准规范，control_+模式名称+状态名称:

```
@Service(value = "control_lcn_starting")
@Slf4j
public class LCNStartingTransaction implements TXLCNTransactionControl {

    private final TransactionControlTemplate transactionControlTemplate;


    @Autowired
    public LCNStartingTransaction(TransactionControlTemplate transactionControlTemplate) {
        this.transactionControlTemplate = transactionControlTemplate;
    }

    @Override
    public void preBusinessCode(TxTransactionInfo info) throws BeforeBusinessException {
        // 创建事务组
        transactionControlTemplate.createGroup(
                info.getGroupId(), info.getUnitId(), info.getTransactionInfo(), info.getTransactionType());

        // LCN 类型事务需要代理资源
        DTXLocal.makeProxy();
    }

    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        DTXLocal.cur().setState(0);
    }

    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) {
        DTXLocal.cur().setState(1);
    }

    @Override
    public void postBusinessCode(TxTransactionInfo info) {
        // RPC 关闭事务组
        transactionControlTemplate.notifyGroup(
                info.getGroupId(), info.getUnitId(), info.getTransactionType(), DTXLocal.cur().getState());
    }
}


```


说明：

若增加的新的模式最好创建一个新的模块，然后调整pom增加该模块的支持即可。