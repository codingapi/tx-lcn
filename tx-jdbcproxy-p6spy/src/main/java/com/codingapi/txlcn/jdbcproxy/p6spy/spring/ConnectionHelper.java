package com.codingapi.txlcn.jdbcproxy.p6spy.spring;

import com.codingapi.txlcn.jdbcproxy.p6spy.common.ConnectionInformation;
import com.codingapi.txlcn.jdbcproxy.p6spy.wrapper.ConnectionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * @author lorne
 * @date 2018/12/12
 * @description
 */
@Component
public class ConnectionHelper {

    @Autowired
    private CompoundJdbcEventListener compoundJdbcEventListener;

    public Connection proxy(Connection connection){
        return ConnectionWrapper.wrap(connection,
                compoundJdbcEventListener,
                ConnectionInformation.fromConnection(connection));
    }


}
