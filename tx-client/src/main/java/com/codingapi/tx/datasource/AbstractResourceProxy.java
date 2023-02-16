package com.codingapi.tx.datasource;


import com.codingapi.tx.annotation.TxTransactionMode;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.datasource.service.DataSourceService;
import com.lorne.core.framework.utils.task.Task;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create by lorne on 2017/8/22
 */

public abstract class AbstractResourceProxy<C,T extends ILCNResource> implements ILCNTransactionControl {


    protected Map<String, ILCNResource> pools = new ConcurrentHashMap<>();


    private Logger logger = LoggerFactory.getLogger(AbstractResourceProxy.class);


    @Autowired
    protected DataSourceService dataSourceService;


    //default size
    protected volatile int maxCount = 5;

    //default time (seconds)
    protected int maxWaitTime = 30;

    protected volatile int nowCount = 0;


    protected volatile boolean hasTransaction = false;

    private volatile boolean isNoTransaction = false;



    // not thread
    protected ICallClose<ILCNResource> subNowCount = new ICallClose<ILCNResource>() {

        @Override
        public void close(ILCNResource connection) {
            //删除回调任务
            Task waitTask = connection.getWaitTask();
            if (waitTask != null) {
                if (!waitTask.isRemove()) {
                    waitTask.remove();
                }
            }
            //从连接池中剔除事务组对应的db连接池
            pools.remove(connection.getGroupId());
            nowCount--;//当前连接池数量-1
        }
    };


    protected abstract C createLcnConnection(C connection, TxTransactionLocal txTransactionLocal);

    protected abstract C createTxcConnection(C connection, TxTransactionLocal txTransactionLocal);

    protected abstract void initDbType();



    protected ILCNResource loadConnection(){
        //从当前线程获取事务远程调用控制对象
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();

        if(txTransactionLocal==null){
            logger.debug("loadConnection -> null !");
            return null;
        }
        if (txTransactionLocal.isReadOnly()) {
            logger.debug("readonly tx don't reuse connection.");
            return null;
        }

        //这里每次获取新的连接的时候会将，事务组id作为key，db连接作为value存储pool中。这样同一个事务组获取的始终是同一个线程，便于后面回滚操作。
        //是否获取旧连接的条件：同一个模块下被多次调用时第一次的事务操作
        ILCNResource old = pools.get(txTransactionLocal.getGroupId());
        if (old != null) {
            //已经有连接了则返回null，后面新建一个连接
            if(txTransactionLocal.isHasConnection()){
                logger.debug("connection is had , transaction get a new connection .");
                return null;
            }

            logger.debug("loadConnection -> old !");
            //标记有连接
            txTransactionLocal.setHasConnection(true);
            return old;
        }
        return null;
    }


    private C createConnection(TxTransactionLocal txTransactionLocal, C connection){
        if (txTransactionLocal.getMode() != null
            && txTransactionLocal.getMode() == TxTransactionMode.TX_MODE_TXC) {
            // 1：txc 模式下没有maxCount的限制 直接创建
            return createTxcConnection(connection, txTransactionLocal);
        }
        //2：若等于最大创建个数，则等待一秒再次获取连接，超过30s则返回源db。
        if (nowCount == maxCount) {
            for (int i = 0; i < maxWaitTime; i++) {
                for(int j=0;j<100;j++){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (nowCount < maxCount) {
                        //封装连接
                        return createLcnConnection(connection, txTransactionLocal);
                    }
                }
            }
        } else if (nowCount < maxCount) {
            //3：低于创建个数，直接创建
            return createLcnConnection(connection, txTransactionLocal);
        } else {
            //4：超过创建个数，说明资源紧张，则返回null。
            logger.info("connection was overload");
            return null;
        }
        return connection;
    }



    protected C initLCNConnection(C connection) {
        logger.debug("initLCNConnection");
        C lcnConnection = connection;
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();

        //没有从缓存中获取到db连接，且事务注解没有设置本服务为只读，则封装db连接。
        if (txTransactionLocal != null&&!txTransactionLocal.isHasConnection()
            && !txTransactionLocal.isReadOnly()) {

            logger.debug("lcn datasource transaction control ");

            //补偿的情况的
//            if (TxCompensateLocal.current() != null) {
//                logger.info("rollback transaction ");
//                return getCompensateConnection(connection,TxCompensateLocal.current());
//            }

            if(StringUtils.isNotEmpty(txTransactionLocal.getGroupId())){

                logger.debug("lcn transaction ");
                //封装连接
                return createConnection(txTransactionLocal, connection);
            }
        }
        logger.debug("load default connection !");
        return lcnConnection;
    }


    @Override
    public boolean hasGroup(String group){
        return pools.containsKey(group);
    }


    @Override
    public boolean executeTransactionOperation() {
        return hasTransaction;
    }


    @Override
    public boolean isNoTransactionOperation() {
        return isNoTransaction;
    }

    @Override
    public void autoNoTransactionOperation() {
        isNoTransaction = true;
    }

    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

}
