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
            Task waitTask = connection.getWaitTask();
            if (waitTask != null) {
                if (!waitTask.isRemove()) {
                    waitTask.remove();
                }
            }

            pools.remove(connection.getGroupId());
            nowCount--;
        }
    };


    protected abstract C createLcnConnection(C connection, TxTransactionLocal txTransactionLocal);

    protected abstract C createTxcConnection(C connection, TxTransactionLocal txTransactionLocal);

    protected abstract void initDbType();



    protected ILCNResource loadConnection(){

        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();

        if(txTransactionLocal==null){
            logger.debug("loadConnection -> null !");
            return null;
        }
        if (txTransactionLocal.isReadOnly()) {
            logger.debug("readonly tx don't reuse connection.");
            return null;
        }

        //是否获取旧连接的条件：同一个模块下被多次调用时第一次的事务操作
        ILCNResource old = pools.get(txTransactionLocal.getGroupId());
        if (old != null) {

            if(txTransactionLocal.isHasConnection()){
                logger.debug("connection is had , transaction get a new connection .");
                return null;
            }

            logger.debug("loadConnection -> old !");
            txTransactionLocal.setHasConnection(true);
            return old;
        }
        return null;
    }


    private C createConnection(TxTransactionLocal txTransactionLocal, C connection){
        if (txTransactionLocal.getMode() != null
            && txTransactionLocal.getMode() == TxTransactionMode.TX_MODE_TXC) {
            // txc 模式下没有maxCount的限制 直接创建
            return createTxcConnection(connection, txTransactionLocal);
        }
        if (nowCount == maxCount) {
            for (int i = 0; i < maxWaitTime; i++) {
                for(int j=0;j<100;j++){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (nowCount < maxCount) {
                        return createLcnConnection(connection, txTransactionLocal);
                    }
                }
            }
        } else if (nowCount < maxCount) {
            return createLcnConnection(connection, txTransactionLocal);
        } else {
            logger.info("connection was overload");
            return null;
        }
        return connection;
    }



    protected C initLCNConnection(C connection) {
        logger.debug("initLCNConnection");
        C lcnConnection = connection;
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();

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
