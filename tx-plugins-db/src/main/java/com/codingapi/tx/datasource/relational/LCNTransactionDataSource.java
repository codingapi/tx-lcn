package com.codingapi.tx.datasource.relational;

import com.codingapi.tx.aop.bean.TxCompensateLocal;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.datasource.AbstractResourceProxy;
import com.codingapi.tx.datasource.ILCNConnection;
import com.codingapi.tx.datasource.relational.txc.TxcDBConnection;
import com.codingapi.tx.datasource.relational.txc.rollback.TxcRollbackService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * 关系型数据库代理连接池对象
 * create by lorne on 2017/7/29
 */

@Component
public class LCNTransactionDataSource extends AbstractResourceProxy<Connection,LCNDBConnection> implements ILCNConnection {


    private org.slf4j.Logger logger = LoggerFactory.getLogger(LCNTransactionDataSource.class);

    @Autowired
    TxcRollbackService txcRollbackService;

    @Override
    protected Connection createLcnConnection(Connection connection, TxTransactionLocal txTransactionLocal) {
        nowCount++;
        if(txTransactionLocal.isHasStart()){
            LCNStartConnection lcnStartConnection = new LCNStartConnection(connection,subNowCount);
            logger.debug("get new start connection - > "+txTransactionLocal.getGroupId());
            pools.put(txTransactionLocal.getGroupId(), lcnStartConnection);
            txTransactionLocal.setHasConnection(true);
            return lcnStartConnection;
        }else {
            LCNDBConnection lcn = new LCNDBConnection(connection, dataSourceService, subNowCount);
            logger.debug("get new connection ->" + txTransactionLocal.getGroupId());
            pools.put(txTransactionLocal.getGroupId(), lcn);
            txTransactionLocal.setHasConnection(true);
            return lcn;
        }
    }

    @Override
    protected Connection createTxcConnection(Connection connection, TxTransactionLocal txTransactionLocal) {
        Connection txc = new TxcDBConnection(connection, txTransactionLocal, dataSourceService, txcRollbackService);
        logger.info("get new txc connection ->" + txTransactionLocal.getGroupId());
        return txc;
    }


    @Override
    protected void initDbType() {
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        if(txTransactionLocal!=null) {
            //设置db类型
            txTransactionLocal.setType("datasource");
        }
        TxCompensateLocal txCompensateLocal = TxCompensateLocal.current();
        if(txCompensateLocal!=null){
            //设置db类型
            txCompensateLocal.setType("datasource");
        }
    }

    @Override
    public Connection getConnection(ProceedingJoinPoint point) throws Throwable {
        //说明有db操作.
        hasTransaction = true;

        initDbType();

        Connection connection = (Connection)loadConnection();
        if(connection==null) {
            connection = initLCNConnection((Connection) point.proceed());
            if(connection==null){
                throw new SQLException("connection was overload");
            }
            return connection;
        }else {
            return connection;
        }
    }
}
