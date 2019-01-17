package com.codingapi.txlcn.manager.db.mybatis;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * Date: 19-1-17 上午10:59
 *
 * @author ujued
 */
public class TxExceptionMapperProvider {

    @SuppressWarnings("unchecked")
    public String deleteByIdList(Map<String, Object> params) {
        return "delete from t_tx_exception where id in (" +
                ((List<Long>) params.get("list"))
                        .stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                ')';
    }
}
