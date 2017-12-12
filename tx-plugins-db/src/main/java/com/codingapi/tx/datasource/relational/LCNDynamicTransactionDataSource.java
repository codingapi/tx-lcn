package com.codingapi.tx.datasource.relational;

import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.datasource.bean.LCNDataSourceLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 关系型数据库动态代理连接池对象
 * create by lorne on 2017/7/29
 */

public class LCNDynamicTransactionDataSource extends LCNTransactionDataSource {


    private Logger logger = LoggerFactory.getLogger(LCNDynamicTransactionDataSource.class);


    private Map<String,DataSource> dataSourceMap = new ConcurrentHashMap<>();


    private String getNowDataSourceKey(){
        if(LCNDataSourceLocal.current()==null){
            return "default";
        }
        return LCNDataSourceLocal.current().getKey();
    }

    @Override
    public boolean hasGroup(String group) {
        return super.hasGroup(getNowDataSourceKey()+group);
    }

    @Override
    protected Connection createLcnConnection(Connection connection, TxTransactionLocal txTransactionLocal) {
        nowCount++;
        if(txTransactionLocal.isHasStart()){
            LCNStartConnection lcnStartConnection = new LCNStartConnection(connection,subNowCount);
            logger.info("get new start connection - > "+txTransactionLocal.getGroupId());
            pools.put(getNowDataSourceKey()+txTransactionLocal.getGroupId(), lcnStartConnection);
            txTransactionLocal.setHasConnection(true);
            return lcnStartConnection;
        }else {
            LCNDBConnection lcn = new LCNDBConnection(connection, dataSourceService, subNowCount);
            logger.info("get new connection ->" + txTransactionLocal.getGroupId());
            pools.put(getNowDataSourceKey()+txTransactionLocal.getGroupId(), lcn);
            txTransactionLocal.setHasConnection(true);
            return lcn;
        }
    }

    public void addDataSource(String key, DataSource dataSource){
        dataSourceMap.put(key,dataSource);
        logger.info("add datasource of "+key);
    }

    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
        addDataSource("default",dataSource);
        logger.info("load default datasource.");
    }

    @Override
    protected DataSource getDataSource() {
        logger.info("getDataSource--->");
        if(LCNDataSourceLocal.current()==null){
            return super.getDataSource();
        }else{
            String key = LCNDataSourceLocal.current().getKey();
            logger.info("get datasource of "+key);
            return dataSourceMap.get(key);
        }
    }
}
