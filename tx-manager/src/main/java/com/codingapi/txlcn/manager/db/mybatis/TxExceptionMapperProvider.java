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


    public String deleteByIdList(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<String> strIds = ((List<Long>) params.get("list")).stream().map(Object::toString).collect(Collectors.toList());
        String stringBuilder = "delete from t_tx_exception where id in (" +
                String.join(", ", strIds) + ')';
        return stringBuilder;
    }
}
